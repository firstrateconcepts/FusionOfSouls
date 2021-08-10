package com.runt9.fusionOfSouls.model.unit.`class`

import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.service.RunState

abstract class UnitClass(val name: String) {
    abstract val synergies: List<ClassSynergy>
    abstract val baseAttackRange: Int
}

abstract class ClassSynergy(val numRequired: Int) {
    abstract fun applyToUnit(unit: GameUnit, state: RunState)
    abstract fun removeFromUnit(unit: GameUnit, state: RunState)
}
