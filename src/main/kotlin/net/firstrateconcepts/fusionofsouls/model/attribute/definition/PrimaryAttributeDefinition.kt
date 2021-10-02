package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent

sealed class PrimaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.PRIMARY
    override val description get() = "$baseDescription Affects ${affects.display()}"
    override val rangeForRandomizer = AttributeRandomRange(5f..10f, 10f..15f)

    override fun getBaseValue(attrs: AttributesComponent) = 100f
    override fun getDisplayValue(value: Float) = value.displayInt()

    protected abstract val baseDescription: String
    protected abstract val affects: Array<AttributeType>

    private fun Array<AttributeType>.display() = joinToString(", ") { it.displayName }
}
