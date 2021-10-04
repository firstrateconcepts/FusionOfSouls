package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.mind
import kotlin.math.sqrt

object CooldownReductionDefinition : SecondaryAttributeDefinition() {
    override val shortName = "CDR"
    override val displayName = "Cooldown Reduction"
    override val baseDescription = "This unit's skill cooldown is divided by this amount."
    override val affectedBy get() = AttributeType.MIND to AttributeType.INSTINCT
    override val rangeForRandomizer = AttributeRandomRange(0.05f..0.1f, 2f..4f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { sqrt(mind() * 0.6f + instinct() * 0.4f) / 10f }
    override fun getDisplayValue(value: Float) = value.displayMultiplier()
}
