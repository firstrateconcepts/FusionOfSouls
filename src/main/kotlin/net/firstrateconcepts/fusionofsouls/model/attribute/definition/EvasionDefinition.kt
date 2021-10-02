package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.luck

object EvasionDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Eva"
    override val displayName = "Evasion"
    override val baseDescription = "Reduces enemy attack rolls by a flat amount, potentially causing them to miss."
    override val affectedBy = arrayOf(AttributeType.INSTINCT, AttributeType.LUCK)
    override val rangeForRandomizer = AttributeRandomRange(1f..2f, 5f..10f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { instinct() * 0.01f + luck() * 0.04f }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
