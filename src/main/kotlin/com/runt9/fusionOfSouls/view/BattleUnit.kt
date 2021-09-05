package com.runt9.fusionOfSouls.view

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.event.TargetChangedEvent
import com.runt9.fusionOfSouls.model.event.TargetRemovedEvent
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.status.StatusEffect
import com.runt9.fusionOfSouls.service.isWithinRange
import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Event
import com.soywiz.korev.EventDispatcher
import com.soywiz.korev.dispatch
import com.soywiz.korio.lang.Cancellable
import com.soywiz.korio.lang.cancel
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.reflect.KClass

enum class BattleUnitState {
    ALIVE, MOVING, ATTACKING
}

class BattleUnit(val unit: GameUnit, val team: Team) : EventDispatcher {
    private val dispatcher = LocalDispatcher()

//    lateinit var body: View
    var gridPos = unit.savedGridPos!!
    val states = mutableSetOf<BattleUnitState>()
    var aggroRange = unit.attackRange
    var previousGridPos: GridPoint? = null
    var movingToGridPos: GridPoint? = null
    var target: BattleUnit? = null
        private set
    var attackJob: Cancellable? = null
    var skillJob: Cancellable? = null
    var statusEffects = mutableMapOf<StatusEffect, Cancellable>()
    var canMove = true
    var canAttack = true
    var canUseSkill = true
    var canChangeTarget = true
    val isAlive get() = states.contains(BattleUnitState.ALIVE)
    val name = unit.name

    var currentHp: Double

//    private lateinit var healthBar: UIProgressBar
//    private lateinit var cooldownBar: UIProgressBar

    init {
//        xy(gridPos.worldX, gridPos.worldY)
        currentHp = unit.secondaryAttrs.maxHp.value
        unit.secondaryAttrs.maxHp.addListener {
            currentHp = min(currentHp, it)
        }
    }

//    suspend fun draw(container: Container) {
//        body = image(unit.unitImage.readBitmap()).center().rotation(team.initialRotation)
//
//        healthBar = uiProgressBar(cellSize.toDouble() * 0.75, 2.0, current = currentHp, maximum = unit.secondaryAttrs.maxHp.value) {
//            // TODO: Not hard-coded
//            xy(-15.0, -20.0)
//            buttonBackColor = RGBA(0, 0, 0, 100)
//        }
//
//        cooldownBar = uiProgressBar(cellSize.toDouble() * 0.75, 2.0, current = 0.0, maximum = unit.skill.modifiedCooldown) {
//            // TODO: Not hard-coded
//            alignTopToBottomOf(healthBar)
//            x = -15.0
//            buttonBackColor = RGBA(0, 0, 200, 100)
//        }
//
//        addTo(container)
//    }

    fun takeDamage(damage: Int) {
        updateHp(-damage)
    }

    fun updateHp(amount: Int) {
        val previousHp = currentHp
        currentHp += amount
//        healthBar.current = currentHp
        val hpText = if (amount >= 0) "Healed" else "Damaged"
        println("[${this.name}]: $hpText for [${abs(amount)}] | From [${previousHp.roundToInt()}] to [${currentHp.roundToInt()}] of [${unit.secondaryAttrs.maxHp.value.roundToInt()}]")
    }

    fun updateCooldown() {
        unit.skill.run {
//            cooldownBar.current = cooldownElapsed
//            cooldownBar.maximum = modifiedCooldown
        }
    }

    fun <T : StatusEffect> addStatusEffect(effect: T, duration: TimeSpan, canStack: Boolean = false, shouldRefresh: Boolean = false) {
        val existingEffects = statusEffects.filter { it::class == effect::class }

        if (shouldRefresh) {
            existingEffects.forEach { (eff, c) ->
                c.cancel()
//                statusEffects[eff] = addFixedUpdater(duration, false) { removeStatusEffect(effect) }
                println("[${this.name}]: Refreshed [${effect.name}] for another [$duration]")
            }
        }

        if (existingEffects.isNotEmpty() && !canStack) {
            return
        }

//        statusEffects[effect] = addFixedUpdater(duration, false) { removeStatusEffect(effect) }

        effect.applyToUnit(this)
        println("[${this.name}]: Added status [${effect.name}] for [$duration]")
    }

    fun removeStatusEffect(effect: StatusEffect, removeAllStacks: Boolean = false) {
        statusEffects.remove(effect)?.cancel()
        effect.removeFromUnit(this)

        if (removeAllStacks) {
            statusEffects.filterKeys { it::class == effect::class }.forEach { (key, _) -> removeStatusEffect(key) }
        }

        println("[${this.name}]: Removed status [${effect.name}]")
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

        println("[${this.name}]: Target changed to [${target.name}]")
    }

    fun removeTarget() {
        if (target == null) {
            return
        }

        val oldTarget = target!!

        target = null
        cancelAttacking()
        dispatcher.dispatch(TargetRemovedEvent(this, oldTarget))

        println("[${this.name}]: Target removed")
    }

    fun cancelAttacking() {
        if (attackJob == null || !states.contains(BattleUnitState.ATTACKING)) {
            return
        }

        attackJob?.cancel()
        attackJob = null
        states.remove(BattleUnitState.ATTACKING)
        println("[${this.name}]: Attacking cancelled")
    }

    fun withinAttackRange(other: Collection<BattleUnit>) = other.any { withinAttackRange(it) }
    fun withinAttackRange(other: BattleUnit) = isWithinRange(other, unit.attackRange)

    override fun <T : Event> addEventListener(clazz: KClass<T>, handler: (T) -> Unit) = dispatcher.addEventListener(clazz, handler)
    override fun <T : Event> dispatch(clazz: KClass<T>, event: T) = dispatcher.dispatch(clazz, event)

    inner class LocalDispatcher : EventDispatcher.Mixin() {
        // Dispatches event both to this view and the underlying GameUnit itself
        override fun <T : Event> dispatch(clazz: KClass<T>, event: T) {
            super.dispatch(clazz, event)
            unit.dispatch(clazz, event)
        }
    }
}
