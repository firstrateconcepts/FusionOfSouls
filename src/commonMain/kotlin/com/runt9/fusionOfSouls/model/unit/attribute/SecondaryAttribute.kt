package com.runt9.fusionOfSouls.model.unit.attribute

import kotlin.math.sqrt

class SecondaryAttribute(
    private val primary: PrimaryAttributes,
    vararg attrsToListen: PrimaryAttribute,
    private val formula: PrimaryAttributes.() -> Double
) : Attribute() {
    init {
        attrsToListen.forEach { it.valueListeners.add { recalculate() } }
    }

    override fun getBase() = primary.formula()
}

class SecondaryAttributes(primary: PrimaryAttributes) {
    val hp = SecondaryAttribute(primary, primary.body, primary.mind) { (body * 1.0) + (mind * 0.5) }
    val damage = SecondaryAttribute(primary, primary.body, primary.instinct) { (body * 0.25) + (instinct * 0.25) }
    val skillMulti = SecondaryAttribute(primary, primary.mind, primary.instinct) { sqrt(mind + instinct) / 10 }
    val defense = SecondaryAttribute(primary, primary.body, primary.luck) { sqrt(body * 0.75) + sqrt(luck * 1.25) }
    val evasion = SecondaryAttribute(primary, primary.instinct, primary.luck) { (instinct * 0.01) + (luck * 0.04) }
    val critThreshold = SecondaryAttribute(primary, primary.mind, primary.luck) { 100 - ((mind * 0.025) + (luck * 0.075)) }
    val critBonus = SecondaryAttribute(primary, primary.body, primary.luck) { sqrt((body * 0.5) + (luck * 1.5)) / 10 }
    val attackSpeed = SecondaryAttribute(primary, primary.body, primary.mind) { ((body * 0.25) + (mind * 0.25)) / 100 }
    val cooldownReduction = SecondaryAttribute(primary, primary.mind, primary.instinct) { sqrt((mind * 0.6) + (instinct * 0.4)) / 10 }
    val all = setOf(hp, damage, skillMulti, defense, evasion, critThreshold, critBonus, attackSpeed, cooldownReduction)

    init {
        all.forEach { it.recalculate() }
    }
}
