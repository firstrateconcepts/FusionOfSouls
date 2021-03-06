package net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BASE_DAMAGE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.COOLDOWN_REDUCTION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.EVASION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ABILITY_MULTI

object InstinctDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Instinct"
    override val displayName = "Instinct"
    override val baseDescription = "Represents the innate focus and reactions of this unit."
    override val affects get() = arrayOf(BASE_DAMAGE, ABILITY_MULTI, EVASION, COOLDOWN_REDUCTION)
}
