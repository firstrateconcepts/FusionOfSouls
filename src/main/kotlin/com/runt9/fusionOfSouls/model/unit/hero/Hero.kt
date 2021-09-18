package com.runt9.fusionOfSouls.model.unit.hero

import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.loot.DefaultPassive
import com.runt9.fusionOfSouls.model.loot.Fusion
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.loot.Rune
import com.runt9.fusionOfSouls.model.loot.TestPassive
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass

class Hero(name: String, unitImage: Texture, ability: Ability, classes: List<UnitClass>) : GameUnit(name, unitImage, ability, classes) {
    val passives = mutableListOf<Passive>()
    val runes = mutableListOf<Rune>()
    val fusions = mutableListOf<Fusion>()
    var xp = 0
    var xpToLevel = 10
    var level = 1

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

    fun addXp(xp: Int): Boolean {
        this.xp += xp
        if (this.xp >= xpToLevel) {
            level++
            this.xp -= xpToLevel
            recalculateXpToLevel()
            return true
        }
        return false
    }

    private fun recalculateXpToLevel() {
        // TODO: There's a formula for this, probably
        xpToLevel = when(level) {
            1 -> 10
            2 -> 20
            3 -> 50
            else -> 120
        }
    }
}
