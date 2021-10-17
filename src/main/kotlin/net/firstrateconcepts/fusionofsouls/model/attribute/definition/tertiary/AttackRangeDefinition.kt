package net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent

object AttackRangeDefinition : TertiaryAttributeDefinition() {
    override val shortName = "Rng"
    override val displayName = "Attack Range"
    override val description = "Max distance at which this unit can attack"
    override val rangeForRandomizer = AttributeRandomRange(0.1f..0.25f, 10f..20f)
    override val clamp = AttributeValueClamp(min = 1f)

    override fun getBaseValue(attrs: AttributesComponent) = 1f
    override fun getDisplayValue(value: Float) = value.displayInt()
}
