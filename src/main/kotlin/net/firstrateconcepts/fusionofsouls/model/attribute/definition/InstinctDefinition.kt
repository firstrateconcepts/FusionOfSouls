package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BASE_DAMAGE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.COOLDOWN_REDUCTION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.EVASION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.SKILL_MULTI

object InstinctDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Instinct"
    override val displayName = "Instinct"
    override val baseDescription = "Represents the innate focus and reactions of this unit."
    override val affects get() = arrayOf(BASE_DAMAGE, SKILL_MULTI, EVASION, COOLDOWN_REDUCTION)
}
