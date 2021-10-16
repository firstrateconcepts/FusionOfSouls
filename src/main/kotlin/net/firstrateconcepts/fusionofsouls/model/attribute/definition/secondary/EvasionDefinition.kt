package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.luck

object EvasionDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Eva"
    override val displayName = "Evasion"
    override val baseDescription = "Reduces enemy hit checks by a flat amount, countered by Accuracy."
    override val affectedBy get() = AttributeType.INSTINCT to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(1f..2f, 5f..10f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { instinct() * 0.01f + luck() * 0.04f }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
