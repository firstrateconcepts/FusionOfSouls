package net.firstrateconcepts.fusionofsouls.model.attribute

import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttacksPerSecondDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttributeDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.BaseDamageDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.BodyDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.CooldownReductionDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.CritBonusDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.CritThresholdDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.DefenseDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.EvasionDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.InstinctDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.LuckDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.MaxHpDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.MindDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.SkillMultiDefinition

enum class AttributeType(val definition: AttributeDefinition) {
    BODY(BodyDefinition),
    MIND(MindDefinition),
    INSTINCT(InstinctDefinition),
    LUCK(LuckDefinition),
    MAX_HP(MaxHpDefinition),
    BASE_DAMAGE(BaseDamageDefinition),
    SKILL_MULTI(SkillMultiDefinition),
    DEFENSE(DefenseDefinition),
    EVASION(EvasionDefinition),
    CRIT_THRESHOLD(CritThresholdDefinition),
    CRIT_BONUS(CritBonusDefinition),
    ATTACKS_PER_SECOND(AttacksPerSecondDefinition),
    COOLDOWN_REDUCTION(CooldownReductionDefinition)
}

enum class AttributePriority { PRIMARY, SECONDARY }
