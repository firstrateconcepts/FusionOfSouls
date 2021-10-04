package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.luck
import net.firstrateconcepts.fusionofsouls.model.component.mind

object CritThresholdDefinition : SecondaryAttributeDefinition() {
    override val shortName = "Crit"
    override val displayName = "Crit Threshold"
    override val baseDescription = "Attack rolls over this amount are crits. Attack rolls proportional to this add or reduce potential damage."
    override val affectedBy get() = AttributeType.MIND to AttributeType.LUCK
    override val rangeForRandomizer = AttributeRandomRange(-2f..-1f, -5f..-3f)

    override fun getBaseValue(attrs: AttributesComponent) = attrs.run { 100f - (mind() * 0.025f + luck() * 0.075f) }
    override fun getDisplayValue(value: Float) = value.displayInt()
}
