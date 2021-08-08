package com.runt9.fusionOfSouls.view

import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.extension.isWithinRange
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.soywiz.korge.ui.UIProgressBar
import com.soywiz.korge.ui.buttonBackColor
import com.soywiz.korge.ui.uiProgressBar
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.View
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.alignTopToBottomOf
import com.soywiz.korge.view.center
import com.soywiz.korge.view.image
import com.soywiz.korge.view.rotation
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.lang.Cancellable
import kotlin.math.min

enum class BattleUnitState {
    MOVING, ATTACKING, USING_SKILL
}

class BattleUnit(val unit: GameUnit, val team: Team) : Container() {
    lateinit var body: View
    var gridPos = unit.savedGridPos!!
    val states = mutableSetOf<BattleUnitState>()
    var aggroRange = 2
    var previousGridPos: GridPoint? = null
    var movingToGridPos: GridPoint? = null
    var target: BattleUnit? = null
    var attackJob: Cancellable? = null
    var skillJob: Cancellable? = null

    var currentHp: Double

    private lateinit var healthBar: UIProgressBar
    private lateinit var cooldownBar: UIProgressBar

    init {
        name = unit.name
        xy(gridPos.worldX, gridPos.worldY)
        currentHp = unit.secondaryAttrs.maxHp.value
        unit.secondaryAttrs.maxHp.addListener {
            currentHp = min(currentHp, it)
        }
    }

    suspend fun draw(container: Container) {
        body = image(unit.unitImage.readBitmap()).center().rotation(team.initialRotation)

        healthBar = uiProgressBar(cellSize.toDouble() * 0.75, 2.0, current = currentHp, maximum = unit.secondaryAttrs.maxHp.value) {
            // TODO: Not hard-coded
            xy(-15.0, -20.0)
            buttonBackColor = RGBA(0, 0, 0, 100)
        }

        cooldownBar = uiProgressBar(cellSize.toDouble() * 0.75, 2.0, current = 0.0, maximum = unit.skill.modifiedCooldown) {
            // TODO: Not hard-coded
            alignTopToBottomOf(healthBar)
            x = -15.0
            buttonBackColor = RGBA(0, 0, 150, 100)
        }

        addTo(container)
    }

    fun takeDamage(damage: Int) {
        currentHp -= damage
        healthBar.current = currentHp
    }

    fun updateCooldown() {
        unit.skill.run {
            cooldownBar.current = cooldownElapsed
            cooldownBar.maximum = modifiedCooldown
        }
    }

    fun canAttack(other: Collection<BattleUnit>) = other.any { canAttack(it) }
    fun canAttack(other: BattleUnit) = isWithinRange(other, unit.attackRange)
}
