package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.extension.diagonalTo
import com.runt9.fusionOfSouls.extension.isWithinRange
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollRequest
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckRequest
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.runt9.fusionOfSouls.model.unit.skill.SkillUseContext
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

class BattleUnitManager(private val runState: RunState, private val gridService: GridService, private val coroutineContext: CoroutineContext, private val attackService: AttackService) {
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

    private fun BattleUnit.initSkill() {
        skillJob = addFixedUpdater(4.timesPerSecond) {
            unit.skill.run {
                if (cooldownElapsed >= modifiedCooldown) {
                    return@run
                }

                updateRemaining(0.25)
                updateCooldown()
            }
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
                // Use skill if we can
                unit.skill.run {
                    val skillUseContext = SkillUseContext(this@addFixedUpdater, runState, this@BattleUnitManager)
                    if (canUseSkill(skillUseContext)) {
                        useSkill(skillUseContext)
                        updateCooldown()
                        checkForKills()
                        return@launchImmediately
                    }
                }

                unitAttackAnimation(this)

                target?.let { t ->
                    performAttack(this@startAttacking, t)
                    checkForKills()
                } ?: cancelAttacking(this)
            }
        }
    }

    suspend fun checkForKills() {
        allUnits.filter { it.currentHp <= 0 }.forEach { unitKilled(it) }
    }

    suspend fun unitAttackAnimation(unit: BattleUnit) {
        unit.run {
            val currentX = pos.x
            val currentY = pos.y

            moveBy(body.rotation.cosine * 3, body.rotation.sine * 3, time = 25.milliseconds)
            moveTo(currentX, currentY, time = 150.milliseconds)
        }
    }

    fun performAttack(attacker: BattleUnit, defender: BattleUnit, damageMultipliers: Collection<Double> = emptyList()) {
        // TODO: Turn print statements into combat log
        val defenderEvasion = defender.unit.secondaryAttrs.evasion.value.toIntRound()
        val attackRoll = attackService.attackRoll(AttackRollRequest(defenderEvasion))
        print("[${attacker.name} -> ${defender.name}]: Rolled [${attackRoll.rawRoll}] against [$defenderEvasion] evasion: ")
        if (attackRoll.finalRoll < 0) {
            println("Attack missed!")
            return
        }

        print("Attack hits! ")
        val critThreshold = attacker.unit.secondaryAttrs.critThreshold.value.toIntRound()
        val critMulti = attacker.unit.secondaryAttrs.critBonus.value
        val critResult = attackService.critCheck(CritCheckRequest(attackRoll, critThreshold, critMulti))
        print("Crit check: [${attackRoll.finalRoll}] vs [$critThreshold]: ")

        if (critResult.isCrit) {
            print("CRITICAL HIT! Unit bonus: [${critMulti.roundDecimalPlaces(2)}] Roll bonus: [${critResult.rollMulti.roundDecimalPlaces(2)}] ")
        } else {
            print("No crit. ")
        }

        val baseDamage = attacker.unit.secondaryAttrs.baseDamage.value
        val defenderDefense = defender.unit.secondaryAttrs.defense.value
        val damage = attackService.damageCalc(DamageCalcRequest(baseDamage.toIntRound(), critResult, defenderDefense, damageMultipliers = damageMultipliers))

        defender.takeDamage(damage.finalDamage)
        println("Raw Damage: [${damage.rawDamage}] against [${defenderDefense.toIntRound()}%] defense | Damage dealt: [${damage.finalDamage}] Defender HP: [${defender.currentHp.toIntRound()} / ${defender.unit.secondaryAttrs.maxHp.value.toIntRound()}]")
    }

    private suspend fun unitKilled(unit: BattleUnit) {
        unit.states.clear()
        cancelAttacking(unit)
        gridService.unblock(unit.gridPos)
        allUnits.filter { it.target == unit }.forEach {
            it.target = null
            cancelAttacking(it)
        }

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

    fun initSkills() {
        playerTeam.forEach { it.initSkill() }
        enemyTeam.forEach { it.initSkill() }
    }
}
