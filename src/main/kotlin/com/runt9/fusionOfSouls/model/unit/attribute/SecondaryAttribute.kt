package com.runt9.fusionOfSouls.model.unit.attribute

import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.ATTACK_SPEED
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.BASE_DAMAGE
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.COOLDOWN_REDUCTION
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.CRIT_BONUS
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.CRIT_THRESHOLD
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.DEFENSE
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.EVASION
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.MAX_HP
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType.SKILL_MULTI
import kotlin.math.sqrt

enum class SecondaryAttributeType(override val attrsAttrSelection: SecondaryAttributes.() -> SecondaryAttribute, override val attrRandomizer: AttributeModifierRandomizer) : AttributeType<SecondaryAttribute, SecondaryAttributes> {
    MAX_HP(SecondaryAttributes::maxHp, randomMaxHp),
    BASE_DAMAGE(SecondaryAttributes::baseDamage, randomBaseDamage),
    SKILL_MULTI(SecondaryAttributes::skillMulti, randomSkillMulti),
    DEFENSE(SecondaryAttributes::defense, randomDefense),
    EVASION(SecondaryAttributes::evasion, randomEvasion),
    CRIT_THRESHOLD(SecondaryAttributes::critThreshold, randomCritThreshold),
    CRIT_BONUS(SecondaryAttributes::critBonus, randomCritMulti),
    ATTACK_SPEED(SecondaryAttributes::attackSpeed, randomAttackSpeed),
    COOLDOWN_REDUCTION(SecondaryAttributes::cooldownReduction, randomCdr);

    override val unitAttrSelection = GameUnit::secondaryAttrs
}


class SecondaryAttribute(
    type: SecondaryAttributeType,
    private val primary: PrimaryAttributes,
    vararg attrsToListen: PrimaryAttribute,
    private val formula: PrimaryAttributes.() -> Double
) : Attribute(type) {
    init {
        attrsToListen.forEach { it.valueListeners.add { recalculate() } }
    }

    override fun getBase() = primary.formula()
}

class SecondaryAttributes(primary: PrimaryAttributes) : Attributes<SecondaryAttribute> {
    val maxHp = SecondaryAttribute(MAX_HP, primary, primary.body, primary.mind) { (body * 1.0) + (mind * 0.5) }
    val baseDamage = SecondaryAttribute(BASE_DAMAGE, primary, primary.body, primary.instinct) { (body * 0.25) + (instinct * 0.25) }
    val skillMulti = SecondaryAttribute(SKILL_MULTI, primary, primary.mind, primary.instinct) { sqrt(mind + instinct) / 10 }
    val defense = SecondaryAttribute(DEFENSE, primary, primary.body, primary.luck) { sqrt(body * 0.75) + sqrt(luck * 1.25) }
    val evasion = SecondaryAttribute(EVASION, primary, primary.instinct, primary.luck) { (instinct * 0.01) + (luck * 0.04) }
    val critThreshold = SecondaryAttribute(CRIT_THRESHOLD, primary, primary.mind, primary.luck) { 100 - ((mind * 0.025) + (luck * 0.075)) }
    val critBonus = SecondaryAttribute(CRIT_BONUS, primary, primary.body, primary.luck) { sqrt((body * 0.5) + (luck * 1.5)) / 10 }
    val attackSpeed = SecondaryAttribute(ATTACK_SPEED, primary, primary.body, primary.mind) { ((body * 0.25) + (mind * 0.25)) / 100 }
    val cooldownReduction = SecondaryAttribute(COOLDOWN_REDUCTION, primary, primary.mind, primary.instinct) { sqrt((mind * 0.6) + (instinct * 0.4)) / 10 }
    override val all = setOf(maxHp, baseDamage, skillMulti, defense, evasion, critThreshold, critBonus, attackSpeed, cooldownReduction)

    init {
        all.forEach(Attribute::recalculate)
    }
}
