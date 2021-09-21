package com.runt9.fusionOfSouls.model.unit.hero

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.loot.Fusion
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.loot.Rune
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.runt9.fusionOfSouls.util.progressBarStyleHeight
import ktx.scene2d.stack
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visProgressBar

class Hero(name: String, unitImage: Texture, ability: Ability, classes: List<UnitClass>) : GameUnit(name, unitImage, ability, classes) {
    val passives = mutableListOf<Passive>()
    val runes = mutableListOf<Rune>()
    val fusions = mutableListOf<Fusion>()
    val fusionAddedListeners = mutableListOf<(Fusion) -> Unit>()
    var xp = 0
    var xpToLevel = 10
    var level = 1

    init {
        // Hero cannot be removed so must start on the grid
        savedGridPos = GridPoint(0.0, 0.0)
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
        fusionAddedListeners.forEach { it(selectedFusion) }
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

    override fun KVisTable.additionalTooltipData() {
        val styleName = "heroTooltipXpBar"
        progressBarStyleHeight(styleName, 10f)
        row()
        stack {
            visProgressBar(0f, xpToLevel.toFloat(), style = styleName) {
                value = xp.toFloat()
                height = 10f
            }
            visLabel("XP: ${xp}/${xpToLevel}", "small") {
                setFontScale(0.5f)
                setAlignment(Align.center)
            }
        }.cell(row = true, width = 50f, space = 5f)
    }
}
