package net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent

object LifestealDefinition : TertiaryAttributeDefinition() {
    override val shortName = "Lfst"
    override val displayName = "Lifesteal"
    override val description = "This percentage of all damage dealt will be regained as HP (25% effective for abilities)"
    override val rangeForRandomizer = AttributeRandomRange(1f..2f, 5f..10f)
    override val clamp = AttributeValueClamp(min = 0f, max = 100f)

    override fun getBaseValue(attrs: AttributesComponent) = 0f
    override fun getDisplayValue(value: Float) = value.displayPercent(0)

}
