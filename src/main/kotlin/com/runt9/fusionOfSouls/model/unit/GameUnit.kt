package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import com.runt9.fusionOfSouls.model.unit.skill.Skill
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.soywiz.korev.EventDispatcher

open class GameUnit(val name: String, val unitImage: Texture, val skill: Skill, startingClasses: List<UnitClass>) : EventDispatcher.Mixin() {
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)
    val attackRange: Int
    val classes = mutableListOf<UnitClass>()

    // Null means on bench
    var savedGridPos: GridPoint? = null

    init {
        attackRange = startingClasses[0].baseAttackRange
        secondaryAttrs.cooldownReduction.addListener(listener = skill::updateCooldown)
        classes.addAll(startingClasses)
    }

    fun reset() {
        secondaryAttrs.purgeTemporaryModifiers()
        primaryAttrs.purgeTemporaryModifiers()
        skill.resetCooldown()
    }
}
