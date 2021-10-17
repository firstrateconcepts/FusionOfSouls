package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.luck

object CritMultiplier : SecondaryAttributeDefinition() {
    override val shortName = "Crit"
    override val displayName = "Crit Multiplier"
    override val baseDescription = "Critical hits multiply their damage by this amount."
    override val affectedBy get() = AttributeType.BODY to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(0.1f..0.15f, 5f..10f)
    override val clamp = AttributeValueClamp(min = 0.5f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { 3f - (50f / body() + 150f / luck()) / (1 + 1f / 3f) }
    override fun getDisplayValue(value: Float) = value.displayMultiplier()
}
