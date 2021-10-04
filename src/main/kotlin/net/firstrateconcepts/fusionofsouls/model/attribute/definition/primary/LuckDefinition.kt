package net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_BONUS
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_THRESHOLD
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.DEFENSE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.EVASION

object LuckDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Luck"
    override val displayName = "Luck"
    override val baseDescription = "Represents how much randomness favors this unit."
    override val affects get() = arrayOf(DEFENSE, EVASION, CRIT_THRESHOLD, CRIT_BONUS)
}
