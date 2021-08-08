package com.runt9.fusionOfSouls.model.unit

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import com.runt9.fusionOfSouls.model.unit.skill.Skill
import com.soywiz.korio.file.VfsFile
import kotlin.random.Random

open class GameUnit(val name: String, val unitImage: VfsFile, val skill: Skill) {
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)
    val attackRange: Int

    // Null means on bench
    var savedGridPos: GridPoint? = null

    init {
        // TODO: Attack range based off of incoming class(s)
        attackRange = Random.nextInt(1, 5)
        secondaryAttrs.cooldownReduction.addListener(listener = skill::updateCooldown)
    }

    fun reset() {
        secondaryAttrs.purgeTemporaryModifiers()
        primaryAttrs.purgeTemporaryModifiers()
        skill.resetCooldown()
    }
}
