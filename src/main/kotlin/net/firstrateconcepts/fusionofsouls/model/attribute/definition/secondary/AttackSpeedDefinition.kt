package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.mind

object AttackSpeedDefinition : SecondaryAttributeDefinition() {
    override val shortName = "ApS"
    override val displayName = "Attack Speed"
    override val baseDescription = "How many times per second this unit will attack."
    override val affectedBy get() = AttributeType.BODY to AttributeType.MIND
    override val rangeForRandomizer = AttributeRandomRange(0.025f..0.05f, 10f..15f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { (body() * 0.25f + mind() * 0.25f) / 100f }
    override fun getDisplayValue(value: Float) = value.displayDecimal()
}
