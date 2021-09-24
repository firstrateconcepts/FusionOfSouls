package com.runt9.fusionOfSouls.view.duringRun

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisDialog
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.model.loot.FusionType
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.smallTextTooltip
import com.runt9.fusionOfSouls.viewportHeight
import ktx.scene2d.KTable
import ktx.scene2d.scene2d
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visScrollPane
import ktx.scene2d.vis.visTable

class BossDialog : VisDialog("Boss", "dialog") {
    init {
        addCloseButton()
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        button("Done")
        closeOnEscape()
    }

    override fun show(stage: Stage?): VisDialog {
        contentTable.apply {
            val fusionPane = scene2d.visTable {
                visLabel("Fusions: ${runState.boss.fusions.size}").cell(growX = true, height = 30f, row = true)

                visScrollPane {
                    setScrollingDisabled(true, false)

                    val fusionInfo: KTable.(String, FusionType) -> KVisTable = { title, type ->
                        visTable {
                            visLabel("${title}:").cell(row = true, growX = true)
                            runState.boss.fusions.filter { it.effect.fusionType == type }.forEach { fusion ->
                                visLabel("- ${fusion.effect.fusionDisplayName}", "small") {
                                    smallTextTooltip(fusion.description)
                                    setFontScale(0.75f)
                                }.cell(row = true, expandX = true, padLeft = 10f, align = Align.left)
                            }
                        }.cell(row = true, growX = true)
                    }

                    visTable {
                        align(Align.top)
                        fusionInfo("Passives", FusionType.PASSIVE)
                        fusionInfo("Ability Augments", FusionType.ABILITY_AUGMENT)
                        fusionInfo("Attribute Modifications", FusionType.ATTR_MODIFICATION)
                    }
                }.cell(grow = true, align = Align.top)
            }
            add(fusionPane).grow()
        }

        return super.show(stage)
    }

    override fun hide() {
        super.hide()
        contentTable.clear()
    }

    override fun getPrefWidth() = (battleWidth * 0.4).toFloat()
    override fun getPrefHeight() = viewportHeight.toFloat() - resourceBarHeight
}
