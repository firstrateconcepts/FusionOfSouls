package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.mind

object MaxHpDefinition : SecondaryAttributeDefinition() {
    override val shortName = "HP"
    override val displayName = "Max HP"
    override val baseDescription = "How much damage the unit can take before it dies."
    override val affectedBy get() = AttributeType.BODY to AttributeType.MIND
    override val rangeForRandomizer = AttributeRandomRange(50f..100f, 10f..20f)
    override val clamp = AttributeValueClamp(min = 1f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { body() * 7.5f + mind() * 2.5f }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
