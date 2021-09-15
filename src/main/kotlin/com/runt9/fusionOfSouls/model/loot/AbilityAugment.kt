package com.runt9.fusionOfSouls.model.loot

import com.runt9.fusionOfSouls.model.unit.GameUnit

class AbilityAugment(override val description: String) : FusableEffect {
    override val fusionType = FusionType.ABILITY_AUGMENT

    override fun applyToUnit(unit: GameUnit) {
        TODO("Not yet implemented")
    }

    override fun removeFromUnit(unit: GameUnit) {
        TODO("Not yet implemented")
    }
}
