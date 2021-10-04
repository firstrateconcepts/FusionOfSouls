package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.mind
import kotlin.math.sqrt

object SkillMultiDefinition : SecondaryAttributeDefinition() {
    override val shortName = "SkMul"
    override val displayName = "Skill Multiplier"
    override val baseDescription = "Skill damage is multiplied by this amount."
    override val affectedBy get() = AttributeType.MIND to AttributeType.INSTINCT
    override val rangeForRandomizer = AttributeRandomRange(0.1f..0.15f, 5f..10f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { sqrt(mind() + instinct()) / 10 }
    override fun getDisplayValue(value: Float) = value.displayMultiplier()
}
