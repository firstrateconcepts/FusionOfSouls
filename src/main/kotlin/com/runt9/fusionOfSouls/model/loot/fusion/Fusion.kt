package com.runt9.fusionOfSouls.model.loot.fusion

import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.unit.GameUnit

enum class FusionType {
    PASSIVE, ATTR_MODIFICATION, ABILITY_AUGMENT
}

interface FusableEffect : GameUnitEffect {
    val fusionType: FusionType
}

class Fusion(val effect: FusableEffect) : GameUnitEffect {
    override val description = effect.description
    // TODO: Check synergy

    override fun applyToUnit(unit: GameUnit) {
        effect.applyToUnit(unit)
    }

    override fun removeFromUnit(unit: GameUnit) {
        effect.removeFromUnit(unit)
    }
}
