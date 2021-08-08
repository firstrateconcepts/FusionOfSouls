package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.extension.diagonalTo
import com.runt9.fusionOfSouls.extension.isAdjacentTo
import com.runt9.fusionOfSouls.extension.isWithinRange
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.tween.hide
import com.soywiz.korge.view.tween.moveBy
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korge.view.tween.rotateTo
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.lang.cancel
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.cosine
import com.soywiz.korma.geom.sine
import com.soywiz.korma.interpolation.Easing
import kotlin.coroutines.CoroutineContext

class BattleUnitManager(private val gridService: GridService, private val coroutineContext: CoroutineContext) {
    val playerTeam = mutableListOf<BattleUnit>()
    val enemyTeam = mutableListOf<BattleUnit>()
    val allUnits
        get() = playerTeam + enemyTeam

    fun clear() {
        playerTeam.forEach(BattleUnit::removeFromParent)
        playerTeam.clear()
        enemyTeam.forEach(BattleUnit::removeFromParent)
        enemyTeam.clear()
    }

    fun enemyTeamOf(unit: BattleUnit) = if (unit.team == Team.PLAYER) enemyTeam else playerTeam

    fun unitMoveComplete(unit: BattleUnit) {
        println("[${unit.name}]: Resetting for new turn")
        unit.previousGridPos = unit.gridPos
        unit.gridPos = unit.movingToGridPos!!
        unit.movingToGridPos = null
    }

    suspend fun checkAttackRange(unit: BattleUnit): Boolean {
        if (!unit.isAdjacentTo(enemyTeamOf(unit))) {
            return false
        }

        if (unit.target == null || !unit.isAdjacentTo(unit.target!!)) {
            unit.target = enemyTeamOf(unit).find { unit.isAdjacentTo(it) }
        }

        println("[${unit.name}]: Attacking so not moving")
        unit.body.rotateTo(Angle.Companion.between(unit.gridPos, unit.target!!.gridPos), time = 150.milliseconds)
        unit.startAttacking()
        return true
    }

    fun checkAggroRange(unit: BattleUnit) {
        if (unit.target != null) return

        if (unit.isWithinRange(enemyTeamOf(unit), unit.aggroRangeFlat)) {
            unit.target = enemyTeamOf(unit).find { unit.isWithinRange(it, unit.aggroRangeFlat) }
            println("[${unit.name}]: Acquired target [${unit.target?.name}]")
        } else {
            unit.aggroRangeFlat++
            println("[${unit.name}]: Aggro range increased")
        }
    }

    suspend fun handleUnitMovement(unit: BattleUnit, nextPoint: GridPoint) {
        println("[${unit.name}]: Moving from [${unit.gridPos}] to [${nextPoint}]")
        unit.movingToGridPos = nextPoint
        gridService.unblock(unit.gridPos)
        gridService.block(nextPoint)
        val newAngle = Angle.Companion.between(unit.gridPos, nextPoint)
        val distance = unit.gridPos.manhattanDistance(nextPoint)

        launchImmediately(coroutineContext) {
            unit.body.rotateTo(newAngle, time = 150.milliseconds)
        }

        launchImmediately(coroutineContext) {
            unit.moveTo(
                nextPoint.worldX,
                nextPoint.worldY,
                time = 1.seconds - (200.milliseconds / distance),
                easing = Easing.LINEAR
            )
        }
    }

    private suspend fun BattleUnit.startAttacking() {
        if (attackJob != null) {
            return
        }

        if (diagonalTo(target!!)) {
            launchImmediately(coroutineContext) {
                moveBy(body.rotation.cosine * 3, body.rotation.sine * 3, time = 150.milliseconds)
            }
        }

        previousGridPos = null
        states.add(BattleUnitState.ATTACKING)
        states.remove(BattleUnitState.MOVING)

        attackJob = addFixedUpdater(unit.secondaryAttrs.attackSpeed.value.timesPerSecond) {
            launchImmediately(coroutineContext) {
                val currentX = pos.x
                val currentY = pos.y

                moveBy(body.rotation.cosine * 3, body.rotation.sine * 3, time = 25.milliseconds)
                moveTo(currentX, currentY, time = 150.milliseconds)

                target?.let { t ->
                    performAttack(this@startAttacking, t)

                    if (t.currentHp.value <= 0) {
                        unitKilled(t)
                        target = null
                        cancelAttacking(this)
                    }
                } ?: cancelAttacking(this)
            }
        }
    }

    suspend fun performAttack(attacker: BattleUnit, defender: BattleUnit) {
    }

    suspend fun unitKilled(unit: BattleUnit) {
        gridService.unblock(unit.gridPos)
        allUnits.filter { it.target == unit }.forEach { it.target = null }
        unit.hide()
        unit.removeFromParent()

        if (unit.team == Team.PLAYER) {
            playerTeam.remove(unit)
            if (playerTeam.isEmpty()) {
                // TODO: Probably need some kinda event bus here
            }
        } else {
            enemyTeam.remove(unit)

            if (enemyTeam.isEmpty()) {
                // TODO: Probably need some kinda event bus here
            }
        }
    }

    fun cancelAttacking(unit: BattleUnit) {
        unit.run {
            attackJob?.cancel()
            attackJob = null
            states.remove(BattleUnitState.ATTACKING)
        }
    }
}
