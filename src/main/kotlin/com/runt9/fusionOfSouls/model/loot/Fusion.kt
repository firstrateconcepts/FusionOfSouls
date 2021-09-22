package com.runt9.fusionOfSouls.model.loot

import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.unit.GameUnit

enum class FusionType(val displayName: String) {
    PASSIVE("Passive"),
    ATTR_MODIFICATION("Attribute Modification"),
    ABILITY_AUGMENT("Ability Augment");
}

interface FusibleEffect : GameUnitEffect {
    val fusionType: FusionType
    val fusionDisplayName: String
}

class Fusion(val effect: FusibleEffect) : GameUnitEffect {
    override val description = effect.description
    // TODO: Check synergy

    override fun applyToUnit(unit: GameUnit) {
        effect.applyToUnit(unit)
    }

    override fun removeFromUnit(unit: GameUnit) {
        effect.removeFromUnit(unit)
    }
}
