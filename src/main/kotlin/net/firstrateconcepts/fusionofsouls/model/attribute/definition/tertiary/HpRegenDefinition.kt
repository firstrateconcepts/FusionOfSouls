package net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent

object HpRegenDefinition : TertiaryAttributeDefinition() {
    override val shortName = "HP/s"
    override val displayName = "HP Regen"
    override val description = "Regenerates this percentage of Max HP every second"
    override val rangeForRandomizer = AttributeRandomRange(0.1f..0.2f, 5f..10f)

    override fun getBaseValue(attrs: AttributesComponent) = 0f
    override fun getDisplayValue(value: Float) = value.displayPercent(2)
}
