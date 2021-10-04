package net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttributeDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.displayName

abstract class SecondaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.SECONDARY
    override val description get() = "$baseDescription Affected by ${affectedBy.display()}"

    protected abstract val baseDescription: String
    protected abstract val affectedBy: Pair<AttributeType, AttributeType>

    private fun Pair<AttributeType, AttributeType>.display() = "${first.displayName} and ${second.displayName}"
}
