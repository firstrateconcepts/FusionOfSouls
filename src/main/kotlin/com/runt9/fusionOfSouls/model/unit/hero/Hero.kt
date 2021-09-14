package com.runt9.fusionOfSouls.model.unit.hero

import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.loot.fusion.Fusion
import com.runt9.fusionOfSouls.model.loot.passive.DefaultPassive
import com.runt9.fusionOfSouls.model.loot.passive.Passive
import com.runt9.fusionOfSouls.model.loot.passive.TestPassive
import com.runt9.fusionOfSouls.model.loot.rune.Rune
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.skill.Skill
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass

class Hero(name: String, unitImage: Texture, skill: Skill, classes: List<UnitClass>) : GameUnit(name, unitImage, skill, classes) {
    val passives = mutableListOf<Passive>()
    val runes = mutableListOf<Rune>()
    val fusions = mutableListOf<Fusion>()

    init {
        // Hero cannot be removed so must start on the grid
        savedGridPos = GridPoint(0.0, 0.0)
        addRune(Rune(Rarity.LEGENDARY))
        fusions.add(Fusion(DefaultPassive()))
        fusions.add(Fusion(TestPassive()))
    }

    fun addRune(rune: Rune) {
        rune.applyToUnit(this)
        runes += rune
    }

    fun removeRune(rune: Rune) {
        rune.removeFromUnit(this)
        runes -= rune
    }

    fun fuseRune(rune: Rune, selectedFusion: Fusion) {
        rune.removeFromUnit(this)
        runes -= rune

        selectedFusion.applyToUnit(this)
        fusions += selectedFusion
        // TODO: Check synergy
    }
}
