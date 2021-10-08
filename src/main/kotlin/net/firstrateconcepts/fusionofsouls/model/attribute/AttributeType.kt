package net.firstrateconcepts.fusionofsouls.model.attribute

import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttributeDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.BodyDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.InstinctDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.LuckDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.MindDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.AttackBonusDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.AttacksPerSecondDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.BaseDamageDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.CooldownReductionDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.CritMultiplier
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.DefenseDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.EvasionDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.MaxHpDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.SkillMultiDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary.AttackRangeDefinition

enum class AttributeType(val definition: AttributeDefinition) {
    // Primary
    BODY(BodyDefinition),
    MIND(MindDefinition),
    INSTINCT(InstinctDefinition),
    LUCK(LuckDefinition),

    // Secondary
    MAX_HP(MaxHpDefinition),
    BASE_DAMAGE(BaseDamageDefinition),
    SKILL_MULTI(SkillMultiDefinition),
    DEFENSE(DefenseDefinition),
    EVASION(EvasionDefinition),
    ATTACK_BONUS(AttackBonusDefinition),
    CRIT_MULTI(CritMultiplier),
    ATTACKS_PER_SECOND(AttacksPerSecondDefinition),
    COOLDOWN_REDUCTION(CooldownReductionDefinition),

    // Tertiary
    ATTACK_RANGE(AttackRangeDefinition)
}

enum class AttributePriority { PRIMARY, SECONDARY, TERTIARY }
