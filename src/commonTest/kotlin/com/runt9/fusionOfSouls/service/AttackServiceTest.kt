package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.Lucky
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollRequest
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollResult
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckRequest
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckResult
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AttackServiceTest {
    val seed = "seed"
    val runState = RunState()
    lateinit var attackService: AttackService

    @BeforeTest
    fun setup() {
        runState.overrideSeed(seed)
        attackService = AttackService(runState)
    }

    @Test
    fun `Test Attack Roll - Basic`() {
        val request = AttackRollRequest(5)
        val result = attackService.attackRoll(request)
        assertEquals(51, result.rawRoll)
        assertEquals(46, result.finalRoll)
    }

    @Test
    fun `Test Attack Roll - With Bonus`() {
        val request = AttackRollRequest(5, attackBonus = 10)
        val result = attackService.attackRoll(request)
        assertEquals(51, result.rawRoll)
        assertEquals(56, result.finalRoll)
    }

    @Test
    fun `Test Attack Roll - With Evasion Reduction`() {
        val request = AttackRollRequest(20, evasionMultiplier = 0.5)
        val result = attackService.attackRoll(request)
        assertEquals(51, result.rawRoll)
        assertEquals(41, result.finalRoll)
    }

    @Test
    fun `Test Attack Roll - Lucky`() {
        val request = AttackRollRequest(5, lucky = Lucky.LUCKY)
        val result = attackService.attackRoll(request)
        assertEquals(51, result.rawRoll)
        assertEquals(46, result.finalRoll)
    }

    @Test
    fun `Test Attack Roll - Unlucky`() {
        val request = AttackRollRequest(5, lucky = Lucky.UNLUCKY)
        val result = attackService.attackRoll(request)
        assertEquals(23, result.rawRoll)
        assertEquals(18, result.finalRoll)
    }

    @Test
    fun `Test Crit Check - Basic Non-Crit`() {
        val request = CritCheckRequest(AttackRollResult(50, 45), 90, 1.25)
        val result = attackService.critCheck(request)
        assertFalse(result.isCrit)
        assertEquals(0.69, result.rollMulti.roundDecimalPlaces(2))
    }

    @Test
    fun `Test Crit Check - Basic Crit`() {
        val request = CritCheckRequest(AttackRollResult(99, 94), 90, 1.25)
        val result = attackService.critCheck(request)
        assertTrue(result.isCrit)
        assertEquals(1.04, result.rollMulti.roundDecimalPlaces(2))
        assertEquals(1.30, result.totalCritMulti.roundDecimalPlaces(2))
    }

    @Test
    fun `Test Crit Check - Crit With Reduction`() {
        val request = CritCheckRequest(AttackRollResult(99, 94), 90, 1.25, 1.5)
        val result = attackService.critCheck(request)
        assertTrue(result.isCrit)
        assertEquals(1.04, result.rollMulti.roundDecimalPlaces(2))
        assertEquals(0.87, result.totalCritMulti.roundDecimalPlaces(2))
    }

    @Test
    fun `Test Damage Calc - Basic Non-Crit`() {
        val request = DamageCalcRequest(50, CritCheckResult(false, 0.75, 1.25, 1.0), 20.0)
        val result = attackService.damageCalc(request)
        assertEquals(38, result.rawDamage)
        assertEquals(30, result.finalDamage)
    }

    @Test
    fun `Test Damage Calc - Basic Crit`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0)
        val result = attackService.damageCalc(request)
        assertEquals(66, result.rawDamage)
        assertEquals(53, result.finalDamage)
    }

    @Test
    fun `Test Damage Calc - Crit With Defense Pen`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0, defenseMultiplier = 0.75)
        val result = attackService.damageCalc(request)
        assertEquals(66, result.rawDamage)
        assertEquals(56, result.finalDamage)
    }

    @Test
    fun `Test Damage Calc - Crit With Flat Reduction`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0, flatDamageReduction = 7)
        val result = attackService.damageCalc(request)
        assertEquals(66, result.rawDamage)
        assertEquals(47, result.finalDamage)
    }

    @Test
    fun `Test Damage Calc - Crit + Defense Pen + Flat Reduction + Extra Multi`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0, 0.75, 7, listOf(1.1, 1.35))
        val result = attackService.damageCalc(request)
        assertEquals(98, result.rawDamage)
        assertEquals(77, result.finalDamage)
    }

}
