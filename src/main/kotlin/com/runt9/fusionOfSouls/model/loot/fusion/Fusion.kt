package com.runt9.fusionOfSouls.model.loot.fusion

import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.unit.GameUnit

class Fusion(val effect: GameUnitEffect) : GameUnitEffect {
    override fun applyToUnit(unit: GameUnit) {
        effect.applyToUnit(unit)
    }

    override fun removeFromUnit(unit: GameUnit) {
        effect.removeFromUnit(unit)
    }
}
