package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.luck

object DefenseDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Def"
    override val displayName = "Defense"
    override val baseDescription = "Incoming damage divided by this amount."
    override val affectedBy get() = AttributeType.BODY to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(0.05f..0.1f, 10f..15f)
    override val clamp = AttributeValueClamp(min = 0.5f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { 2f - (75f / body() + 125f / luck()) / 2 }
    override fun getDisplayValue(value: Float) = value.displayDecimal()
}
