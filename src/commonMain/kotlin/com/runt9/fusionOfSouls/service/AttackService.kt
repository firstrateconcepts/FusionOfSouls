package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.Lucky
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollRequest
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollResult
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckRequest
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckResult
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcResult
import com.soywiz.kmem.toIntRound
import kotlin.math.max
import kotlin.math.min

// TODO: Unit tests!
class AttackService(val runState: RunState) {
    private fun rawRoll() = runState.rng.nextInt(0, 100)

    fun attackRoll(request: AttackRollRequest): AttackRollResult {
        val rawRoll = when(request.lucky) {
            Lucky.LUCKY -> max(rawRoll(), rawRoll())
            Lucky.UNLUCKY -> min(rawRoll(), rawRoll())
            Lucky.NORMAL -> rawRoll()
        }

        val finalRoll = rawRoll + request.attackBonus - request.totalEvasion
        return AttackRollResult(rawRoll, finalRoll)
    }

    fun critCheck(request: CritCheckRequest): CritCheckResult {
        val critResult = request.attackRoll.finalRoll - request.critThreshold
        val rollMulti = 100.0 / (100 - critResult)
        return CritCheckResult(critResult >= 0, rollMulti, request.critMulti, request.critReduction)
    }

    fun damageCalc(request: DamageCalcRequest): DamageCalcResult {
        val baseDamage = request.baseDamage

        val totalDamage = if (request.critResult.isCrit) {
            (baseDamage * request.critResult.totalCritMulti).toIntRound()
        } else {
            (baseDamage * request.critResult.rollMulti).toIntRound()
        }

        val rawDamage = totalDamage * request.totalDamageMultiplier
        val finalDamage = (rawDamage - request.flatDamageReduction) / request.finalDefensePercent

        return DamageCalcResult(rawDamage.toIntRound(), finalDamage.toIntRound())
    }
}
