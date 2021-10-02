package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.mind

object MaxHpDefinition : SecondaryAttributeDefinition() {
    override val shortName = "HP"
    override val displayName = "Max HP"
    override val baseDescription = "How much damage the unit can take before it dies."
    override val affectedBy = arrayOf(AttributeType.BODY, AttributeType.MIND)
    override val rangeForRandomizer = AttributeRandomRange(15f..25f, 10f..20f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { body() * 1f + mind() * 0.5f }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
