package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.mind

object AbilityMultiDefinition : SecondaryAttributeDefinition() {
    override val shortName = "A(x)"
    override val displayName = "Ability Multiplier"
    override val baseDescription = "Ability damage is multiplied by this amount."
    override val affectedBy get() = AttributeType.MIND to AttributeType.INSTINCT
    override val rangeForRandomizer = AttributeRandomRange(0.1f..0.15f, 5f..10f)
    override val clamp = AttributeValueClamp(min = 0.5f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { 3f - (100f / mind() + 100f / instinct()) / (1 + 1f / 3f) }
    override fun getDisplayValue(value: Float) = value.displayMultiplier()
}
