package net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACK_SPEED
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.COOLDOWN_REDUCTION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ACCURACY
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MAX_HP
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ABILITY_MULTI

object MindDefinition : PrimaryAttributeDefinition() {
    override val shortName = "Mind"
    override val displayName = "Mind"
    override val baseDescription = "Represents the mental capacity of this unit."
    override val affects get() = arrayOf(MAX_HP, ABILITY_MULTI, ACCURACY, ATTACK_SPEED, COOLDOWN_REDUCTION)
}
