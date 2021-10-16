package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.luck
import net.firstrateconcepts.fusionofsouls.model.component.mind

object AccuracyDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Acc"
    override val displayName = "Accuracy"
    override val baseDescription = "Flat amount added to hit checks, countered by Evasion."
    override val affectedBy get() = AttributeType.MIND to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(1f..2f, 10f..20f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { (mind() * 0.02f) + (luck() * 0.03f) }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
