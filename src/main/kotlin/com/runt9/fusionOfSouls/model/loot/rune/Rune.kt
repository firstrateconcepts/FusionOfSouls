package com.runt9.fusionOfSouls.model.loot.rune

import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.loot.Rarity.COMMON
import com.runt9.fusionOfSouls.model.loot.Rarity.LEGENDARY
import com.runt9.fusionOfSouls.model.loot.Rarity.RARE
import com.runt9.fusionOfSouls.model.loot.Rarity.UNCOMMON
import com.runt9.fusionOfSouls.model.loot.fusion.Fusion
import com.runt9.fusionOfSouls.model.loot.passive.DefaultPassive
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeType
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributeType
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType
import kotlin.random.Random

private val Rarity.numRuneAttrs: Int
    get() = when(this) {
        COMMON -> 1
        UNCOMMON -> 2
        RARE, LEGENDARY -> 3
    }

class Rune(val rarity: Rarity) : GameUnitEffect {
    val modifiers = generateModifiers(rarity)
    val passives = if (rarity == LEGENDARY) listOf(randomLegendaryPassive()) else emptyList()
    val fusion = Fusion((modifiers + passives).random())

    override fun applyToUnit(unit: GameUnit) {
        modifiers.forEach { it.applyToUnit(unit) }
        passives.forEach { it.applyToUnit(unit) }
    }

    override fun removeFromUnit(unit: GameUnit) {
        modifiers.forEach { it.removeFromUnit(unit) }
        passives.forEach { it.removeFromUnit(unit) }
    }
}

// TODO: Actual passive pool
fun randomLegendaryPassive() = DefaultPassive()

fun generateModifiers(rarity: Rarity): List<AttributeModifierEffect<*, *>> {
    val generatedSoFar = mutableListOf<AttributeType<*, *>>()
    val output = mutableListOf<AttributeModifierEffect<*, *>>()

    repeat(rarity.numRuneAttrs) {
        val randomAttr = if (Random.nextBoolean()) {
            PrimaryAttributeType.values().filter { !generatedSoFar.contains(it) }.random()
        } else {
            SecondaryAttributeType.values().filter { !generatedSoFar.contains(it) }.random()
        }

        output += AttributeModifierEffect(randomAttr, randomAttr.attrRandomizer.getRandom(rarity))
    }

    return output
}
