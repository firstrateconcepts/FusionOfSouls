package com.runt9.fusionOfSouls.model.unit.attack

data class CritCheckRequest(val attackRoll: AttackRollResult, val critThreshold: Int, val critMulti: Double, val critReduction: Double = 1.0)

data class CritCheckResult(val isCrit: Boolean, val rollMulti: Double, val unitCritMulti: Double, val critReduction: Double) {
    val totalCritMulti = (rollMulti * unitCritMulti) / critReduction
}
