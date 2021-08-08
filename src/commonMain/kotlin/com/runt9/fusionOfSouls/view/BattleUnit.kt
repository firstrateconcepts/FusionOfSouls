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
import com.soywiz.korge.view.center
import com.soywiz.korge.view.image
import com.soywiz.korge.view.rotation
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.RGBA
import com.soywiz.korio.lang.Cancellable

enum class BattleUnitState {
    MOVING, ATTACKING, USING_SKILL
}

class BattleUnit(val unit: GameUnit, val team: Team) : Container() {
    val body: View
    var gridPos = unit.savedGridPos!!
    var currentHp = unit.secondaryAttrs.maxHp.value
    val states = mutableSetOf<BattleUnitState>()
    var aggroRange = 2
    var previousGridPos: GridPoint? = null
    var movingToGridPos: GridPoint? = null
    var target: BattleUnit? = null
    var attackJob: Cancellable? = null
    var healthBar: UIProgressBar

    init {
        name = unit.name

        xy(gridPos.worldX, gridPos.worldY)
        body = image(unit.unitImage).center().rotation(team.initialRotation)

        healthBar = uiProgressBar(cellSize.toDouble() * 0.75, 2.0, current = currentHp, maximum = unit.secondaryAttrs.maxHp.value) {
            // TODO: Not hard-coded
            xy(-15.0, -20.0)
            buttonBackColor = RGBA(0, 0, 0, 100)
        }
    }

    fun takeDamage(damage: Int) {
        currentHp -= damage
        healthBar.current = currentHp
    }

    fun canAttack(other: Collection<BattleUnit>) = other.any { canAttack(it) }
    fun canAttack(other: BattleUnit) = isWithinRange(other, unit.attackRange)
}
