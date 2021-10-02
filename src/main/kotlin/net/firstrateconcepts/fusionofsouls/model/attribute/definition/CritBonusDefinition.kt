package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.luck
import kotlin.math.sqrt

object CritBonusDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Crit X"
    override val displayName = "Crit Bonus"
    override val baseDescription = "Critical hits multiply their damage by this amount."
    override val affectedBy get() = AttributeType.BODY to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(0.1f..0.15f, 5f..10f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { sqrt(body() * 0.5f + luck() * 1.5f) / 10f }
    override fun getDisplayValue(value: Float) = value.displayMultiplier()
}
