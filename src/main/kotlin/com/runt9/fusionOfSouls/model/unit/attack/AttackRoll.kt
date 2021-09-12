package com.runt9.fusionOfSouls.model.unit.attack

import com.runt9.fusionOfSouls.model.Lucky
import kotlin.math.roundToInt

data class AttackRollRequest(val evasion: Int, val attackBonus: Int = 0, val evasionMultiplier: Double = 1.0, val lucky: Lucky = Lucky.NORMAL) {
    val totalEvasion = (evasion * evasionMultiplier).roundToInt()
}

data class AttackRollResult(val rawRoll: Int, val finalRoll: Int)
