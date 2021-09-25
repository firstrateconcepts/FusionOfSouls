package com.runt9.fusionOfSouls.service

import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor
import com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.event.BeforeDamageEvent
import com.runt9.fusionOfSouls.model.event.OnHitEvent
import com.runt9.fusionOfSouls.model.event.WhenHitEvent
import com.runt9.fusionOfSouls.model.minus
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.ability.AbilityUseContext
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollRequest
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckRequest
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.runt9.fusionOfSouls.model.unit.hero.Hero
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.korev.dispatch
import com.soywiz.korma.geom.angleTo
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.math.roundDecimalPlaces
import kotlinx.coroutines.launch
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.async.KtxAsync
import ktx.async.interval
import ktx.log.info
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// TODO: Needs to tear down units on returning to main menu
class BattleUnitManager(private val gridService: GridService, private val attackService: AttackService) {
    fun enemyTeamOf(unit: BattleUnit) = if (unit.team == Team.PLAYER) runState.battleContext.enemyTeam else runState.battleContext.playerTeam

    fun unitMoveComplete(unit: BattleUnit) {
        unit.previousGridPos = unit.gridPos
        unit.gridPos = unit.movingToGridPos!!
        unit.movingToGridPos = null
    }

    suspend fun checkAttackRange(unit: BattleUnit): Boolean {
        if (!unit.withinAttackRange(enemyTeamOf(unit))) {
            return false
        }

        if (unit.target == null || (!unit.withinAttackRange(unit.target!!) && unit.canChangeTarget)) {
            enemyTeamOf(unit).find { unit.withinAttackRange(it) }?.let { unit.changeTarget(it) }
        }

        val rotation = unit.gridPos.angleTo(unit.target!!.gridPos).degrees.toFloat()
        info("[${unit.name}]") { "Rotating from ${unit.unit.rotation} to $rotation degrees" }
        unit.unit.addAction(rotateTo(rotation, 0.15f))
        unit.startAttacking()
        return true
    }

    fun checkAggroRange(unit: BattleUnit) {
        if (unit.target != null) return

        if (unit.isWithinRange(enemyTeamOf(unit), unit.aggroRange)) {
            enemyTeamOf(unit).find { unit.isWithinRange(it, unit.aggroRange) }?.let { unit.changeTarget(it) }
            unit.aggroRange = unit.unit.attackRange
        } else {
            unit.aggroRange++
        }
    }

    fun handleUnitMovement(unit: BattleUnit, nextPoint: GridPoint) {
        info("[${unit.name}]") { "Moving from [${unit.gridPos}] to [${nextPoint}]" }
        unit.movingToGridPos = nextPoint
        gridService.unblock(unit.gridPos)
        gridService.block(nextPoint)
        val newAngle = unit.gridPos.angleTo(nextPoint).degrees.toFloat()
        val diff = nextPoint - unit.gridPos

        KtxAsync.launch {
            info("[${unit.name}]") { "Rotating from ${unit.unit.rotation} to $newAngle degrees" }
            // TODO: If > 180, it's rotating all the way around. See if we can figure out a rotateBy instead
            val action = rotateTo(newAngle, 0.15f)
            action.isUseShortestDirection = true
            unit.unit.addAction(action)
        }

        KtxAsync.launch {
            unit.addAction(moveBy(diff.worldX.toFloat(), diff.worldY.toFloat(), 1f))
        }
    }

    private fun BattleUnit.initSkill() {
        unit.ability.resetCooldown()
        skillJob = interval(0.25f) {
            unit.ability.run {
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

        previousGridPos = null
        states.add(BattleUnitState.ATTACKING)
        states.remove(BattleUnitState.MOVING)

        attackJob = interval((1 / unit.secondaryAttrs.attackSpeed.value).toFloat()) {
            if (!states.contains(BattleUnitState.ATTACKING)) {
                return@interval
            }

            KtxAsync.launch {
                // Use skill if we can
                unit.ability.run {
                    val abilityUseContext = AbilityUseContext(this@startAttacking, runState, this@BattleUnitManager)
                    if (canUseSkill(abilityUseContext)) {
                        useSkill(abilityUseContext)
                        updateCooldown()
                        checkForKills()
                        return@launch
                    }
                }

                unitAttackAnimation(this@startAttacking)

                target?.let { t ->
                    performAttack(this@startAttacking, t)
                    checkForKills()
                } ?: cancelAttacking()
            }
        }
    }

    private fun checkForKills() {
        runState.battleContext.allUnits.filter { it.currentHp <= 0 }.forEach { unitKilled(it) }
    }

    fun unitAttackAnimation(unit: BattleUnit) {
        unit.run {
            val currentX = x
            val currentY = y

            // TODO: This isn't properly using rotation, will need to figure it out
            unit += moveBy(cos(this.unit.rotation) * 3, sin(this.unit.rotation * 3), 0.025f) then moveTo(currentX, currentY, 0.15f)
        }
    }

    fun performAttack(attacker: BattleUnit, defender: BattleUnit, damageMultipliers: Collection<Double> = emptyList()) {
        // TODO: Turn print statements into combat log
        val defenderEvasion = defender.unit.secondaryAttrs.evasion.value.roundToInt()
        val attackRoll = attackService.attackRoll(AttackRollRequest(defenderEvasion))
        val tag = "[${attacker.name} -> ${defender.name}]"
        val combatStr = StringBuilder()
        combatStr.append("Rolled [${attackRoll.rawRoll}] against [$defenderEvasion] evasion: ")
        if (attackRoll.finalRoll < 0) {
            combatStr.append("Attack missed!")
            info(tag) { combatStr.toString() }
            return
        }

        combatStr.append("Attack hits! ")
        val critThreshold = attacker.unit.secondaryAttrs.critThreshold.value.roundToInt()
        val critMulti = attacker.unit.secondaryAttrs.critBonus.value
        val critResult = attackService.critCheck(CritCheckRequest(attackRoll, critThreshold, critMulti))
        combatStr.append("Crit check: [${attackRoll.finalRoll}] vs [$critThreshold]: ")

        if (critResult.isCrit) {
            combatStr.append("CRITICAL HIT! Unit bonus: [${critMulti.roundDecimalPlaces(2)}] Roll bonus: [${critResult.rollMulti.roundDecimalPlaces(2)}] ")
        } else {
            combatStr.append("No crit. ")
        }

        val baseDamage = attacker.unit.secondaryAttrs.baseDamage.value
        val defenderDefense = defender.unit.secondaryAttrs.defense.value
        val damageCalcRequest = DamageCalcRequest(baseDamage.roundToInt(), critResult, defenderDefense, damageMultipliers = damageMultipliers)
        attacker.dispatch(BeforeDamageEvent(attacker, defender, attackRoll, critResult, damageCalcRequest))
        val damage = attackService.damageCalc(damageCalcRequest)

        defender.takeDamage(damage.finalDamage)
        attacker.dispatch(OnHitEvent(attacker, defender, attackRoll, critResult, damage))
        defender.dispatch(WhenHitEvent(attacker, defender, attackRoll, critResult, damage))
        combatStr.append("Raw Damage: [${damage.rawDamage}] against [${defenderDefense.roundToInt()}%] defense | Damage dealt: [${damage.finalDamage}] Defender HP: [${defender.currentHp.roundToInt()} / ${defender.unit.secondaryAttrs.maxHp.value.roundToInt()}]")
        info(tag) { combatStr.toString() }
    }

    private fun unitKilled(unit: BattleUnit) {
        unit.states.clear()
        unit.cancelAttacking()
        gridService.unblock(unit.gridPos)
        runState.battleContext.allUnits.filter { it.target == unit }.forEach {
            it.removeTarget()
        }

        if (unit.team == Team.PLAYER) {
            runState.battleContext.playerTeam.remove(unit)
            runState.battleContext.flawless = false
            if (unit.unit is Hero) {
                runState.battleContext.heroLived = false
            }
        } else {
            runState.battleContext.enemyTeam.remove(unit)
        }

        unit += fadeOut(0.25f) then removeActor()
        unit.unit.reset()
    }

    fun initSkills() {
        runState.battleContext.playerTeam.forEach { it.initSkill() }
        runState.battleContext.enemyTeam.forEach { it.initSkill() }
    }
}
