package com.runt9.fusionOfSouls.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.runt9.fusionOfSouls.model.Lucky
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollRequest
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollResult
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckRequest
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckResult
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.soywiz.korma.math.roundDecimalPlaces
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AttackServiceTest {
    val seed = "seed"
    lateinit var attackService: AttackService

    @BeforeEach
    fun setup() {
        runState.overrideSeed(seed)
        attackService = AttackService()
    }

    @Test
    fun `Test Attack Roll - Basic`() {
        val request = AttackRollRequest(5)
        val result = attackService.attackRoll(request)
        assertThat(result.rawRoll).isEqualTo(51)
        assertThat(result.finalRoll).isEqualTo(46)
    }

    @Test
    fun `Test Attack Roll - With Bonus`() {
        val request = AttackRollRequest(5, attackBonus = 10)
        val result = attackService.attackRoll(request)
        assertThat(result.rawRoll).isEqualTo(51)
        assertThat(result.finalRoll).isEqualTo(56)
    }

    @Test
    fun `Test Attack Roll - With Evasion Reduction`() {
        val request = AttackRollRequest(20, evasionMultiplier = 0.5)
        val result = attackService.attackRoll(request)
        assertThat(result.rawRoll).isEqualTo(51)
        assertThat(result.finalRoll).isEqualTo(41)
    }

    @Test
    fun `Test Attack Roll - Lucky`() {
        val request = AttackRollRequest(5, lucky = Lucky.LUCKY)
        val result = attackService.attackRoll(request)
        assertThat(result.rawRoll).isEqualTo(51)
        assertThat(result.finalRoll).isEqualTo(46)
    }

    @Test
    fun `Test Attack Roll - Unlucky`() {
        val request = AttackRollRequest(5, lucky = Lucky.UNLUCKY)
        val result = attackService.attackRoll(request)
        assertThat(result.rawRoll).isEqualTo(23)
        assertThat(result.finalRoll).isEqualTo(18)
    }

    @Test
    fun `Test Crit Check - Basic Non-Crit`() {
        val request = CritCheckRequest(AttackRollResult(50, 45), 90, 1.25)
        val result = attackService.critCheck(request)
        assertThat(result.isCrit).isFalse()
        assertThat(result.rollMulti.roundDecimalPlaces(2)).isEqualTo(0.69)
    }

    @Test
    fun `Test Crit Check - Basic Crit`() {
        val request = CritCheckRequest(AttackRollResult(99, 94), 90, 1.25)
        val result = attackService.critCheck(request)
        assertThat(result.isCrit).isTrue()
        assertThat(result.rollMulti.roundDecimalPlaces(2)).isEqualTo(1.04)
        assertThat(result.totalCritMulti.roundDecimalPlaces(2)).isEqualTo(1.30)
    }

    @Test
    fun `Test Crit Check - Crit With Reduction`() {
        val request = CritCheckRequest(AttackRollResult(99, 94), 90, 1.25, 1.5)
        val result = attackService.critCheck(request)
        assertThat(result.isCrit).isTrue()
        assertThat(result.rollMulti.roundDecimalPlaces(2)).isEqualTo(1.04)
        assertThat(result.totalCritMulti.roundDecimalPlaces(2)).isEqualTo(0.87)
    }

    @Test
    fun `Test Damage Calc - Basic Non-Crit`() {
        val request = DamageCalcRequest(50, CritCheckResult(false, 0.75, 1.25, 1.0), 20.0)
        val result = attackService.damageCalc(request)
        assertThat(result.rawDamage).isEqualTo(38)
        assertThat(result.finalDamage).isEqualTo(30)
    }

    @Test
    fun `Test Damage Calc - Basic Extra Base Damage`() {
        val request = DamageCalcRequest(50, CritCheckResult(false, 0.75, 1.25, 1.0), 20.0, additionalBaseDamage = mutableListOf(10))
        val result = attackService.damageCalc(request)
        assertThat(result.rawDamage).isEqualTo(45)
        assertThat(result.finalDamage).isEqualTo(36)
    }

    @Test
    fun `Test Damage Calc - Basic Crit`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0)
        val result = attackService.damageCalc(request)
        assertThat(result.rawDamage).isEqualTo(66)
        assertThat(result.finalDamage).isEqualTo(53)
    }

    @Test
    fun `Test Damage Calc - Crit With Defense Pen`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0, defenseMultiplier = 0.75)
        val result = attackService.damageCalc(request)
        assertThat(result.rawDamage).isEqualTo(66)
        assertThat(result.finalDamage).isEqualTo(56)
    }

    @Test
    fun `Test Damage Calc - Crit With Flat Reduction`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0, flatDamageReduction = 7)
        val result = attackService.damageCalc(request)
        assertThat(result.rawDamage).isEqualTo(66)
        assertThat(result.finalDamage).isEqualTo(47)
    }

    @Test
    fun `Test Damage Calc - Crit + Defense Pen + Flat Reduction + Extra Multi + Extra Base Damage`() {
        val request = DamageCalcRequest(50, CritCheckResult(true, 1.05, 1.25, 1.0), 20.0, 0.75, 7, mutableListOf(10), listOf(1.1, 1.35))
        val result = attackService.damageCalc(request)
        assertThat(result.rawDamage).isEqualTo(117)
        assertThat(result.finalDamage).isEqualTo(94)
    }

}
