package com.runt9.fusionOfSouls.model.unit.hero

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.loot.fusion.Fusion
import com.runt9.fusionOfSouls.model.loot.passive.Passive
import com.runt9.fusionOfSouls.model.loot.rune.Rune
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.skill.Skill
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.soywiz.korio.file.VfsFile

class Hero(name: String, unitImage: VfsFile, skill: Skill, classes: List<UnitClass>) : GameUnit(name, unitImage, skill, classes) {
    val passives = mutableListOf<Passive>()
    val runes = mutableListOf<Rune>()
    val fusions = mutableListOf<Fusion>()

    init {
        // Hero cannot be removed so must start on the grid
        savedGridPos = GridPoint(0.0, 0.0)
        addRune(Rune(Rarity.COMMON))
    }

    fun addRune(rune: Rune) {
        rune.applyToUnit(this)
        runes += rune
    }

    fun removeRune(rune: Rune) {
        rune.removeFromUnit(this)
        runes -= rune
    }

    fun fuseRune(rune: Rune) {
        rune.removeFromUnit(this)
        runes -= rune

        val fusion = rune.fusion
        fusion.applyToUnit(this)
        fusions += fusion
        // TODO: Check synergy
    }
}
