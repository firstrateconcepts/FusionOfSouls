package net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttributeDefinition

abstract class TertiaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.TERTIARY
    override val clamp = AttributeValueClamp()
}
