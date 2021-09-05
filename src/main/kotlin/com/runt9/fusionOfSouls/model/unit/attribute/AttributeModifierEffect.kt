package com.runt9.fusionOfSouls.model.unit.attribute

import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.unit.GameUnit

// TODO: Bunch of random stuff in here, needs to be from run seed
class AttributeModifierEffect<A : Attribute, T : Attributes<A>>(val type: AttributeType<A, T>, val modifier: AttributeModifier) : GameUnitEffect {
    override fun applyToUnit(unit: GameUnit) {
        unit.getTargetedAttribute().addModifier(modifier)
    }

    override fun removeFromUnit(unit: GameUnit) {
        unit.getTargetedAttribute().removeModifier(modifier)
    }

    private fun GameUnit.getTargetedAttribute(): Attribute {
        val unitAttr = type.unitAttrSelection(this)
        return type.attrsAttrSelection(unitAttr)
    }
}
