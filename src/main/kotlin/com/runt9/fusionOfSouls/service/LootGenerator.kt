package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeType
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributeType
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType
import kotlin.random.Random

// TODO: Ensure this is actually random? 4 straight instinct increases may not be valid?
fun generateModifiers(rarity: Rarity, count: Int): List<AttributeModifierEffect<*, *>> {
    val generatedSoFar = mutableListOf<AttributeType<*, *>>()
    val output = mutableListOf<AttributeModifierEffect<*, *>>()

    repeat(count) {
        val randomAttr = if (Random.nextBoolean()) {
            PrimaryAttributeType.values().filter { !generatedSoFar.contains(it) }.random()
        } else {
            SecondaryAttributeType.values().filter { !generatedSoFar.contains(it) }.random()
        }

        output += AttributeModifierEffect(randomAttr, randomAttr.attrRandomizer.getRandom(rarity))
        generatedSoFar += randomAttr
    }

    return output
}
