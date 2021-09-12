package com.runt9.fusionOfSouls.model.unit.attack

data class DamageCalcRequest(
    val baseDamage: Int,
    val critResult: CritCheckResult,
    val defense: Double,
    val defenseMultiplier: Double = 1.0,
    val flatDamageReduction: Int = 0,
    val additionalBaseDamage: MutableList<Int> = mutableListOf(),
    val damageMultipliers: Collection<Double> = emptyList()
) {
    val totalDamageMultiplier = if (damageMultipliers.isEmpty()) 1.0 else damageMultipliers.reduce { total, next -> total * next }
    val finalDefensePercent = 100.0 / (100 - (defense * defenseMultiplier))
}

data class DamageCalcResult(val rawDamage: Int, val finalDamage: Int)
