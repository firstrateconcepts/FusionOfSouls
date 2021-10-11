package net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACK_SPEED
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BASE_DAMAGE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_MULTI
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.DEFENSE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MAX_HP

object BodyDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Body"
    override val displayName = "Body"
    override val baseDescription = "Represents the physical prowess of this unit."
    override val affects get() = arrayOf(MAX_HP, BASE_DAMAGE, DEFENSE, CRIT_MULTI, ATTACK_SPEED)
}
