package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.extension.diagonalTo
import com.runt9.fusionOfSouls.extension.isWithinRange
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.kmem.toIntRound
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
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

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
        unit.previousGridPos = unit.gridPos
        unit.gridPos = unit.movingToGridPos!!
        unit.movingToGridPos = null
    }

    suspend fun checkAttackRange(unit: BattleUnit): Boolean {
        if (!unit.canAttack(enemyTeamOf(unit))) {
            return false
        }

        if (unit.target == null || !unit.canAttack(unit.target!!)) {
            unit.target = enemyTeamOf(unit).find { unit.canAttack(it) }
        }

        unit.body.rotateTo(Angle.Companion.between(unit.gridPos, unit.target!!.gridPos), time = 150.milliseconds)
        unit.startAttacking()
        return true
    }

    fun checkAggroRange(unit: BattleUnit) {
        if (unit.target != null) return

        if (unit.isWithinRange(enemyTeamOf(unit), unit.aggroRange)) {
            unit.target = enemyTeamOf(unit).find { unit.isWithinRange(it, unit.aggroRange) }
            println("[${unit.name}]: Acquired target [${unit.target?.name}]")
        } else {
            unit.aggroRange++
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
            if (!states.contains(BattleUnitState.ATTACKING)) {
                return@addFixedUpdater
            }

            launchImmediately(coroutineContext) {
                val currentX = pos.x
                val currentY = pos.y

                moveBy(body.rotation.cosine * 3, body.rotation.sine * 3, time = 25.milliseconds)
                moveTo(currentX, currentY, time = 150.milliseconds)

                target?.let { t ->
                    performAttack(this@startAttacking, t)

                    if (t.currentHp <= 0) {
                        unitKilled(t)
                        target = null
                        cancelAttacking(this)
                    }
                } ?: cancelAttacking(this)
            }
        }
    }

    private fun performAttack(attacker: BattleUnit, defender: BattleUnit) {
        // TODO: Turn print statements into combat log
        val defenderEvasion = defender.unit.secondaryAttrs.evasion.value
        val attackRoll = Random.nextInt(0, 100)
        val attackResult = attackRoll - defenderEvasion
        print("[${attacker.name} -> ${defender.name}]: Rolled [$attackRoll] against [$defenderEvasion] evasion: ")
        if (attackResult < 0) {
            println("Attack missed!")
            return
        }

        print("Attack hits! ")
        val critThreshold = attacker.unit.secondaryAttrs.critThreshold.value
        val critResult = attackResult - critThreshold
        print("Crit check: [$attackResult] vs [$critThreshold]: ")
        val baseDamage = attacker.unit.secondaryAttrs.baseDamage.value
        val totalDamage = if (critResult >= 0) {
            val critMulti = attacker.unit.secondaryAttrs.critBonus.value
            val rollMulti = 100 / (100 - critResult)
            print("CRITICAL HIT! Unit bonus: [${critMulti.roundDecimalPlaces(2)}] Roll bonus: [${rollMulti.roundDecimalPlaces(2)}] ")
            baseDamage * critMulti * rollMulti
        } else {
            print("No crit. ")
            baseDamage
        }

        val defenderDefense = defender.unit.secondaryAttrs.defense.value
        val finalDamage = (totalDamage * ((100 - defenderDefense) / 100)).toIntRound()

        defender.takeDamage(finalDamage)
        println("Raw Damage: [${totalDamage.toIntRound()}] against [${defenderDefense.toIntRound()}%] defense | Damage dealt: [$finalDamage] Defender HP: [${defender.currentHp.toIntRound()} / ${defender.unit.secondaryAttrs.maxHp.value.toIntRound()}]")
    }

    private suspend fun unitKilled(unit: BattleUnit) {
        unit.states.clear()
        cancelAttacking(unit)
        gridService.unblock(unit.gridPos)
        allUnits.filter { it.target == unit }.forEach { it.target = null }

        if (unit.team == Team.PLAYER) {
            playerTeam.remove(unit)
        } else {
            enemyTeam.remove(unit)
        }

        unit.hide()
        unit.removeFromParent()
    }

    fun cancelAttacking(unit: BattleUnit) {
        unit.run {
            attackJob?.cancel()
            attackJob = null
            states.remove(BattleUnitState.ATTACKING)
        }
    }
}
