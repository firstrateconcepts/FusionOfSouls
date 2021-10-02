package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType

sealed class SecondaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.SECONDARY
    override val description get() = "$baseDescription Affected by ${affectedBy.display()}"

    protected abstract val baseDescription: String
    protected abstract val affectedBy: Pair<AttributeType, AttributeType>

    private fun Pair<AttributeType, AttributeType>.display() = "${first.displayName} and ${second.displayName}"
}
