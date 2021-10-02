package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACKS_PER_SECOND
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BASE_DAMAGE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_BONUS
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.DEFENSE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MAX_HP

object BodyDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Body"
    override val displayName = "Body"
    override val baseDescription = "Represents the physical prowess of this unit."
    override val affects = arrayOf(MAX_HP, BASE_DAMAGE, DEFENSE, CRIT_BONUS, ATTACKS_PER_SECOND)
}
