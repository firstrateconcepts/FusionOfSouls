package com.runt9.fusionOfSouls.model.loot

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.kotcrab.vis.ui.VisUI
import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.loot.Rarity.COMMON
import com.runt9.fusionOfSouls.model.loot.Rarity.LEGENDARY
import com.runt9.fusionOfSouls.model.loot.Rarity.RARE
import com.runt9.fusionOfSouls.model.loot.Rarity.UNCOMMON
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.service.generateModifiers
import com.runt9.fusionOfSouls.util.rectPixmapTexture
import ktx.scene2d.KGroup
import ktx.scene2d.textTooltip
import ktx.style.defaultStyle
import ktx.style.get
import ktx.style.textTooltip

private val Rarity.numRuneAttrs: Int
    get() = when(this) {
        COMMON -> 1
        UNCOMMON -> 2
        RARE, LEGENDARY -> 3
    }

class Rune(rarity: Rarity) : GameUnitEffect, Container<Image>(), KGroup {
    override val description by lazy { generateDescription() }
    private val modifiers = generateModifiers(rarity, rarity.numRuneAttrs)
    private val passives = if (rarity == LEGENDARY) listOf(randomLegendaryPassive()) else emptyList()

    init {
        actor = Image(rectPixmapTexture(25, 25, Color.BLUE))
        textTooltip(description) { tt ->
            wrap = true

            tt.setInstant(true)
            tt.setStyle(VisUI.getSkin().textTooltip("smallerTooltip", extend = defaultStyle) {
                label = VisUI.getSkin()["small"]
                setFontScale(0.75f)
            })
        }
    }

    override fun applyToUnit(unit: GameUnit) {
        modifiers.forEach { it.applyToUnit(unit) }
        passives.forEach { it.applyToUnit(unit) }
    }

    override fun removeFromUnit(unit: GameUnit) {
        modifiers.forEach { it.removeFromUnit(unit) }
        passives.forEach { it.removeFromUnit(unit) }
    }

    private fun generateDescription(): String {
        val sb = StringBuilder()
        modifiers.forEach {
            sb.append("${it.description}\n")
        }
        passives.forEach {
            sb.append("${it.description}\n")
        }
        return sb.toString()
    }
}

// TODO: Actual passive pool
fun randomLegendaryPassive() = DefaultPassive()
