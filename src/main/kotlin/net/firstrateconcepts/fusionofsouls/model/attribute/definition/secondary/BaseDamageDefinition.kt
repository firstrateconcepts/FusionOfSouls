package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.instinct

object BaseDamageDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Dmg"
    override val displayName = "Base Damage"
    override val baseDescription = "The base amount of damage done by attacks and abilities. Reduced by enemy Defense."
    override val affectedBy get() = AttributeType.BODY to AttributeType.INSTINCT
    override val rangeForRandomizer = AttributeRandomRange(5f..10f, 10f..15f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { body() * 0.5f + instinct() * 0.5f }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
