package com.runt9.fusionOfSouls.model.unit.unitClass

import com.runt9.fusionOfSouls.model.GameUnitEffect

abstract class UnitClass(val name: String) {
    abstract val synergies: List<ClassSynergy>
    abstract val baseAttackRange: Int
}

abstract class ClassSynergy(val numRequired: Int) : GameUnitEffect
