package net.firstrateconcepts.fusionofsouls.model.attribute

import net.firstrateconcepts.fusionofsouls.model.attribute.definition.AttributeDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.BodyDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.InstinctDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.LuckDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.MindDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.AbilityMultiDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.AccuracyDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.AttackSpeedDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.BaseDamageDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.CooldownReductionDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.CritMultiplier
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.DefenseDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.EvasionDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.secondary.MaxHpDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary.AttackRangeDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary.HpRegenDefinition
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.tertiary.LifestealDefinition

enum class AttributeType(val definition: AttributeDefinition) {
    // Primary
    BODY(BodyDefinition),
    MIND(MindDefinition),
    INSTINCT(InstinctDefinition),
    LUCK(LuckDefinition),

    // Secondary
    MAX_HP(MaxHpDefinition),
    BASE_DAMAGE(BaseDamageDefinition),
    ABILITY_MULTI(AbilityMultiDefinition),
    DEFENSE(DefenseDefinition),
    EVASION(EvasionDefinition),
    ACCURACY(AccuracyDefinition),
    CRIT_MULTI(CritMultiplier),
    ATTACK_SPEED(AttackSpeedDefinition),
    COOLDOWN_REDUCTION(CooldownReductionDefinition),

    // Tertiary
    ATTACK_RANGE(AttackRangeDefinition),
    LIFESTEAL(LifestealDefinition),
    HP_REGEN(HpRegenDefinition),
}

enum class AttributePriority { PRIMARY, SECONDARY, TERTIARY }
