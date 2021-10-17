package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.mind

object CooldownReductionDefinition : SecondaryAttributeDefinition() {
    override val shortName = "CDR"
    override val displayName = "Cooldown Reduction"
    override val baseDescription = "This unit's ability cooldown is divided by this amount."
    override val affectedBy get() = AttributeType.MIND to AttributeType.INSTINCT
    override val rangeForRandomizer = AttributeRandomRange(0.05f..0.1f, 5f..10f)
    override val clamp = AttributeValueClamp(min = 0.5f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { 2f - (100f / mind() + 100f / instinct()) / 2 }
    override fun getDisplayValue(value: Float) = value.displayDecimal()
}
