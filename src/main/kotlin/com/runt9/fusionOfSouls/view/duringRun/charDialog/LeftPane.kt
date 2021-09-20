package com.runt9.fusionOfSouls.view.duringRun.charDialog

import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.progressBarStyleHeight
import com.runt9.fusionOfSouls.util.scaledLabel
import com.runt9.fusionOfSouls.view.duringRun.charDialog.tab.AbilityTab
import com.runt9.fusionOfSouls.view.duringRun.charDialog.tab.AttrsTab
import com.runt9.fusionOfSouls.view.duringRun.charDialog.tab.RuneTab
import ktx.scene2d.KTable
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.vis.addTabContentsTo
import ktx.scene2d.vis.tabbedPane
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visProgressBar
import ktx.scene2d.vis.visTable
import ktx.style.get
import ktx.style.tabbedPane
import ktx.style.visTextButton

fun leftPane() = scene2d.visTable {
    buildCharInfo().cell(spaceBottom = 10f, padRight = 5f, growX = true, row = true)

    skin.visTextButton("smallText", "toggle") {
        font = skin["small-font"]
    }
    skin.tabbedPane("smallText", "default") {
        buttonStyle = skin["smallText"]
        draggable = false
    }

    visTable {
        val paneInfo = tabbedPane("smallText") { cell ->
            cell.growX().row()
            add(AttrsTab())
            add(RuneTab())
            add(AbilityTab())
        }

        val tabContent = visTable().cell(grow = true, padLeft = 10f, padRight = 10f)
        paneInfo.addTabContentsTo(tabContent)
        paneInfo.switchTab(0)
    }.cell(grow = true, padRight = 5f)
}

private const val xpBarStyleName = "charDialogXpBar"
private fun KTable.buildCharInfo() = visTable {
    val hero = runState.hero
    visTable {
        visImage(hero.unitImage).cell(row = true)

        progressBarStyleHeight(xpBarStyleName, 10f)

        val currentLevel = hero.level
        val xpToLevel = hero.xpToLevel
        val currentXp = hero.xp

        stack {
            visProgressBar(0f, xpToLevel.toFloat(), style = xpBarStyleName) {
                value = currentXp.toFloat()
                height = 10f
            }
            visLabel("XP: ${currentXp}/${xpToLevel}", "small") {
                setFontScale(0.5f)
                setAlignment(Align.center)
            }
        }.cell(row = true, width = 50f, space = 5f)

        scaledLabel("Level: $currentLevel").cell(row = true, expand = true, align = Align.center)
        hero.classes.forEach {
            scaledLabel(it.name).cell(row = true, expand = true, align = Align.center)
        }
    }.cell(grow = true)

    visTable {
        hero.primaryAttrs.all.forEach { attr ->
            visLabel(attr.type.displayName).cell(align = Align.left, expandX = true)
            visLabel(attr.displayValue()) {
                attr.addListener {
                    setText(attr.displayValue())
                }
            }.cell(align = Align.center, row = true)
        }
    }.cell(grow = true)
}
