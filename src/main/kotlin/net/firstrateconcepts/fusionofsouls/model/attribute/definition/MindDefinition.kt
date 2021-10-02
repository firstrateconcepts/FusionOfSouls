package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACKS_PER_SECOND
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.COOLDOWN_REDUCTION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_THRESHOLD
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MAX_HP
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.SKILL_MULTI

object MindDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Mind"
    override val displayName = "Mind"
    override val baseDescription = "Represents the mental capacity of this unit."
    override val affects get() = arrayOf(MAX_HP, SKILL_MULTI, CRIT_THRESHOLD, ATTACKS_PER_SECOND, COOLDOWN_REDUCTION)
}
