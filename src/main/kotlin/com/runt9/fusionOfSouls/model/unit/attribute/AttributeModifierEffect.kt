package com.runt9.fusionOfSouls.model.unit.attribute

import com.runt9.fusionOfSouls.model.loot.fusion.FusableEffect
import com.runt9.fusionOfSouls.model.loot.fusion.FusionType
import com.runt9.fusionOfSouls.model.unit.GameUnit

// TODO: Bunch of random stuff in here, needs to be from run seed
class AttributeModifierEffect<A : Attribute, T : Attributes<A>>(private val type: AttributeType<A, T>, private val modifier: AttributeModifier) : FusableEffect {
    override val fusionType = FusionType.ATTR_MODIFICATION
    override val description by lazy { generateDescription() }

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

    private fun generateDescription(): String {
        val sb = StringBuilder()
        sb.append("${type.displayName} ")
        val hasFlat = modifier.flatModifier != 0.0
        if (hasFlat) {
            val incRed = if (modifier.flatModifier > 0.0) "Increased" else "Reduced"
            sb.append("$incRed by ${modifier.flatModifier}")
        }
        if (modifier.percentModifier != 0.0) {
            val incRed = if (modifier.percentModifier > 0.0) "Increased" else "Reduced"
            val and = if (hasFlat) " and " else " "
            sb.append("$and $incRed by ${modifier.percentModifier}%")
        }

        return sb.toString()
    }
}
