package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.luck
import kotlin.math.sqrt

object DefenseDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Def"
    override val displayName = "Defense"
    override val baseDescription = "Incoming damage is reduced by this percentage."
    override val affectedBy get() = AttributeType.BODY to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(1f..2f, 10f..15f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { sqrt(body() * 0.75f) + sqrt(luck() * 1.25f) }
    override fun getDisplayValue(value: Float) = value.displayPercent()
}
