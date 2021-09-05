package com.runt9.fusionOfSouls.model.event

import com.runt9.fusionOfSouls.model.unit.attack.AttackRollResult
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckResult
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcResult
import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.korev.Event

data class OnHitEvent(
    val attacker: BattleUnit,
    val defender: BattleUnit,
    val attackRoll: AttackRollResult,
    val critResult: CritCheckResult,
    val damage: DamageCalcResult
): Event()

data class WhenHitEvent(
    val attacker: BattleUnit,
    val defender: BattleUnit,
    val attackRoll: AttackRollResult,
    val critResult: CritCheckResult,
    val damage: DamageCalcResult
): Event()

class BeforeDamageEvent(
    val attacker: BattleUnit,
    val defender: BattleUnit,
    val attackRoll: AttackRollResult,
    val critResult: CritCheckResult,
    val damageCalcRequest: DamageCalcRequest
): Event()
