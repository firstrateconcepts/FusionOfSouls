package com.runt9.fusionOfSouls.view

import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveToAligned
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Disableable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer.Task
import com.kotcrab.vis.ui.widget.VisProgressBar
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.event.TargetChangedEvent
import com.runt9.fusionOfSouls.model.event.TargetRemovedEvent
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.status.StatusEffect
import com.runt9.fusionOfSouls.service.BattleStatus
import com.runt9.fusionOfSouls.service.isWithinRange
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.progressBarStyleHeight
import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Event
import com.soywiz.korev.EventDispatcher
import com.soywiz.korev.dispatch
import com.soywiz.korma.geom.degrees
import ktx.actors.plusAssign
import ktx.async.schedule
import ktx.log.info
import ktx.scene2d.KGroup
import ktx.scene2d.actor
import ktx.scene2d.vis.visProgressBar
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.reflect.KClass

enum class BattleUnitState {
    ALIVE, MOVING, ATTACKING
}

class BattleUnit(val unit: GameUnit, val team: Team) : EventDispatcher, WidgetGroup(), KGroup, Disableable {
    private val dispatcher = LocalDispatcher()
    private val unitBarStyle = "unitBar"

    var gridPos = unit.savedGridPos!!
    val states = mutableSetOf<BattleUnitState>()
    var aggroRange = unit.attackRange
    var previousGridPos: GridPoint? = null
    var movingToGridPos: GridPoint? = null
    var target: BattleUnit? = null
        private set
    var attackJob: Task? = null
    var skillJob: Task? = null
    var statusEffects = mutableMapOf<StatusEffect, Task>()
    var canMove = true
    var canAttack = true
    var canUseSkill = true
    var canChangeTarget = true
    val isAlive get() = states.contains(BattleUnitState.ALIVE)
    private var isDisabled = false

    var currentHp: Double

    private val healthBar: VisProgressBar
    private val cooldownBar: VisProgressBar

    init {
        isTransform = false
        name = unit.name
        currentHp = unit.secondaryAttrs.maxHp.value

        unit.secondaryAttrs.maxHp.addListener {
            currentHp = min(currentHp, it)
        }

        actor(unit)
        setOrigin(Align.center)
        unit.setOrigin(Align.center)
        unit.rotation = team.initialRotation.degrees.toFloat()
        unit.battleUnit = this

        progressBarStyleHeight(unitBarStyle, 2f)

        val barDefaults: VisProgressBar.(Float) -> Unit = { yOffset ->
            setAnimateDuration(0.25f)
            width = (cellSize * 0.75).toFloat()
            height = 2f
            setOrigin(Align.center)
            y = cellSize.toFloat() - yOffset
        }

        healthBar = visProgressBar(0f, unit.secondaryAttrs.maxHp.value.toFloat(), style = unitBarStyle) {
            value = this@BattleUnit.currentHp.toFloat()
            barDefaults(5f)
        }

        cooldownBar = visProgressBar(0f, unit.ability.modifiedCooldown.toFloat(), style = unitBarStyle) {
            barDefaults(7f)
        }

        runState.statusListeners.add { setDisabled(it == BattleStatus.DURING) }
    }

    fun takeDamage(damage: Int) {
        updateHp(-damage)
    }

    fun updateHp(amount: Int) {
        val previousHp = currentHp
        currentHp += amount
        healthBar.value = currentHp.toFloat()
        val hpText = if (amount >= 0) "Healed" else "Damaged"
        info("[${this.name}]") { "$hpText for [${abs(amount)}] | From [${previousHp.roundToInt()}] to [${currentHp.roundToInt()}] of [${unit.secondaryAttrs.maxHp.value.roundToInt()}]" }
    }

    fun updateCooldown() {
        unit.ability.run {
            cooldownBar.value = cooldownElapsed.toFloat()
            cooldownBar.setRange(0f, modifiedCooldown.toFloat())
        }
    }

    fun <T : StatusEffect> addStatusEffect(effect: T, duration: TimeSpan, canStack: Boolean = false, shouldRefresh: Boolean = false) {
        val existingEffects = statusEffects.filter { it::class == effect::class }

        if (shouldRefresh) {
            existingEffects.forEach { (eff, c) ->
                c.cancel()
                statusEffects[eff] = schedule(duration.seconds.toFloat()) { removeStatusEffect(effect) }
                info("[${this.name}]") { "Refreshed [${effect.name}] for another [$duration]" }
            }
        }

        if (existingEffects.isNotEmpty() && !canStack) {
            return
        }

        statusEffects[effect] = schedule(duration.seconds.toFloat()) { removeStatusEffect(effect) }

        effect.applyToUnit(this)
        info("[${this.name}]") { "Added status [${effect.name}] for [$duration]" }
    }

    fun removeStatusEffect(effect: StatusEffect, removeAllStacks: Boolean = false) {
        statusEffects.remove(effect)?.cancel()
        effect.removeFromUnit(this)

        if (removeAllStacks) {
            statusEffects.filterKeys { it::class == effect::class }.forEach { (key, _) -> removeStatusEffect(key) }
        }

        info("[${this.name}]") { "Removed status [${effect.name}]" }
    }

    fun changeTarget(target: BattleUnit) {
        if (!canChangeTarget || this.target == target) {
            return
        }

        val oldTarget = this.target
        this.target = target

        if (!withinAttackRange(target)) {
            cancelAttacking()
        }

        dispatcher.dispatch(TargetChangedEvent(this, oldTarget, target))

        info("[${this.name}]") { "Target changed to [${target.name}]" }
    }

    fun removeTarget() {
        if (target == null) {
            return
        }

        val oldTarget = target!!

        target = null
        cancelAttacking()
        dispatcher.dispatch(TargetRemovedEvent(this, oldTarget))

        info("[${this.name}]") { "Target removed" }
    }

    fun cancelAttacking() {
        if (attackJob == null || !states.contains(BattleUnitState.ATTACKING)) {
            return
        }

        attackJob?.cancel()
        attackJob = null
        states.remove(BattleUnitState.ATTACKING)
        info("[${this.name}]") { "Attacking cancelled" }
    }

    override fun remove(): Boolean {
        attackJob?.cancel()
        skillJob?.cancel()
        unit.reset()
        return super.remove()
    }

    override fun setDisabled(isDisabled: Boolean) {
        this.isDisabled = isDisabled
    }

    override fun isDisabled() = this.isDisabled

    fun withinAttackRange(other: Collection<BattleUnit>) = other.any { withinAttackRange(it) }
    fun withinAttackRange(other: BattleUnit) = isWithinRange(other, unit.attackRange)

    override fun <T : Event> addEventListener(clazz: KClass<T>, handler: (T) -> Unit) = dispatcher.addEventListener(clazz, handler)
    override fun <T : Event> dispatch(clazz: KClass<T>, event: T) = dispatcher.dispatch(clazz, event)

    fun setInitialPosition() {
        this += moveToAligned(gridPos.worldX.toFloat(), gridPos.worldY.toFloat(), Align.center)
    }

    inner class LocalDispatcher : EventDispatcher.Mixin() {
        // Dispatches event both to this view and the underlying GameUnit itself
        override fun <T : Event> dispatch(clazz: KClass<T>, event: T) {
            super.dispatch(clazz, event)
            unit.dispatch(clazz, event)
        }
    }
}
