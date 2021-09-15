package com.runt9.fusionOfSouls.service

import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.loot.DefaultPassive
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.ability.DefaultAbility
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass

class UnitGenerator {
    var unitNum = 0
    fun generateUnit(rarity: Rarity, image: Texture): BasicUnit {
        val classes = generateClasses()
        val passive = generatePassive(rarity, classes)
        val ability = getAbilityFromPool(classes)
        val attrMod = generateAttributeModifier(rarity, classes)

        return BasicUnit("Unit Name ${unitNum++}", image, ability, passive, attrMod, classes)
    }

    private fun generateAttributeModifier(rarity: Rarity, classes: List<UnitClass>): AttributeModifierEffect<*, *> {
        return generateModifiers(rarity, 1)[0]
    }

    private fun getAbilityFromPool(classes: List<UnitClass>): Ability {
        return DefaultAbility()
    }

    private fun generateClasses(): List<UnitClass> {
        return listOf(TankClass())
    }

    private fun generatePassive(rarity: Rarity, classes: List<UnitClass>): Passive {
        return DefaultPassive()
    }
}
