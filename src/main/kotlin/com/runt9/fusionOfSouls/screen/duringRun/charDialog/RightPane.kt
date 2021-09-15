package com.runt9.fusionOfSouls.screen.duringRun.charDialog

import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.model.loot.FusionType
import com.runt9.fusionOfSouls.service.runState
import ktx.scene2d.KTable
import ktx.scene2d.scene2d
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visScrollPane
import ktx.scene2d.vis.visTable

fun rightPane() = scene2d.visTable {
    val fusions = runState.hero.fusions
    visTable {
        visLabel("Fusions: ${fusions.size}/${runState.fusionCap}")
    }.cell(growX = true, height = 30f, row = true)

    visScrollPane {
        setScrollingDisabled(true, false)

        val fusionInfo: KTable.(String, FusionType) -> KVisTable = { title, type ->
            visTable {
                visLabel("${title}:").cell(row = true, growX = true)
                fusions.filter { it.effect.fusionType == type }.forEach {
                    visLabel("- ${it.description}", "small") {
                        wrap = true
                        setFontScale(0.75f)
                    }.cell(row = true, growX = true, padLeft = 10f)
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

