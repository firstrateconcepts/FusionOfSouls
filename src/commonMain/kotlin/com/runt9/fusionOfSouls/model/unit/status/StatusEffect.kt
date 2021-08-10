package com.runt9.fusionOfSouls.model.unit.status

import com.runt9.fusionOfSouls.view.BattleUnit

// TODO: Probably need buff/debuff differentiation
interface StatusEffect {
    val name: String

    fun applyToUnit(unit: BattleUnit)
    fun removeFromUnit(unit: BattleUnit)
}
