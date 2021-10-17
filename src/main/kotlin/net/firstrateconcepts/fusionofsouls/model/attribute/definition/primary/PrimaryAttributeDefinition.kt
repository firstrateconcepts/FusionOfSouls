package net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttributeDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.displayName
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent

abstract class PrimaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.PRIMARY
    override val description get() = "$baseDescription Affects ${affects.display()}"
    override val rangeForRandomizer = AttributeRandomRange(5f..10f, 10f..15f)
    override val clamp = AttributeValueClamp(min = 50f)

    override fun getBaseValue(attrs: AttributesComponent) = 100f
    override fun getDisplayValue(value: Float) = value.displayInt()

    protected abstract val baseDescription: String
    abstract val affects: Array<AttributeType>

    private fun Array<AttributeType>.display() = joinToString(", ") { it.displayName }
}
