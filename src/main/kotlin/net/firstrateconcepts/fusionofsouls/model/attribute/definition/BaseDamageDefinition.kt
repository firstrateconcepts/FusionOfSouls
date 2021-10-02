package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.instinct

object BaseDamageDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Dmg"
    override val displayName = "Base Damage"
    override val baseDescription = "The base amount of damage done by attacks and skills. Reduced by enemy Defense."
    override val affectedBy = arrayOf(AttributeType.BODY, AttributeType.INSTINCT)
    override val rangeForRandomizer = AttributeRandomRange(3f..7f, 10f..15f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { body() * 0.25f + instinct() * 0.25f }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
