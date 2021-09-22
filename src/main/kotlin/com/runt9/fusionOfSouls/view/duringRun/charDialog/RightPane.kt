package com.runt9.fusionOfSouls.view.duringRun.charDialog

import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.model.event.FusionAddedEvent
import com.runt9.fusionOfSouls.model.loot.Fusion
import com.runt9.fusionOfSouls.model.loot.FusionType
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.observableLabel
import com.runt9.fusionOfSouls.util.smallTextTooltip
import ktx.scene2d.KTable
import ktx.scene2d.scene2d
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visScrollPane
import ktx.scene2d.vis.visTable

fun rightPane() = scene2d.visTable {
    visTable {
        observableLabel(runState.hero.fusionAddedListeners) { "Fusions: ${runState.hero.fusions.size}/${runState.fusionCap}" }
    }.cell(growX = true, height = 30f, row = true)

    visScrollPane {
        setScrollingDisabled(true, false)

        val fusionInfo: KTable.(String, FusionType) -> KVisTable = { title, type ->
            visTable {
                visLabel("${title}:").cell(row = true, growX = true)
                runState.hero.fusions.filter { it.effect.fusionType == type }.forEach {
                    addFusion(it)
                }

                runState.hero.addListener { event ->
                    if (event is FusionAddedEvent && event.fusionAdded.effect.fusionType == type) {
                        addFusion(event.fusionAdded)
                        return@addListener true
                    }

                    return@addListener false
                }
            }.cell(row = true, growX = true)
        }

        visTable {
            align(Align.top)
            fusionInfo("Passives", FusionType.PASSIVE)
            fusionInfo("Ability Augments", FusionType.ABILITY_AUGMENT)
            fusionInfo("Attribute Modifications", FusionType.ATTR_MODIFICATION)
            // TODO: Synergies
        }
    }.cell(grow = true, align = Align.top)
}

fun KVisTable.addFusion(fusion: Fusion) = visLabel("- ${fusion.effect.fusionDisplayName}", "small") {
    smallTextTooltip(fusion.description)
    // Would prefer to have wrap on but messes up tooltip without explicit width, but with wrap and fill on, is too wide and tooltip
    // shows up even when not hovering over the text iself, so /shrug
//    wrap = true
    setFontScale(0.75f)
}.cell(row = true, expandX = true, padLeft = 10f, align = Align.left)
