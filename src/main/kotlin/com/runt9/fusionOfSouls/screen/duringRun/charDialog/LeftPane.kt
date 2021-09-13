package com.runt9.fusionOfSouls.screen.duringRun.charDialog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.runt9.fusionOfSouls.screen.duringRun.charDialog.tab.AbilityTab
import com.runt9.fusionOfSouls.screen.duringRun.charDialog.tab.AttrsTab
import com.runt9.fusionOfSouls.screen.duringRun.charDialog.tab.RuneTab
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.progressBarStyleHeight
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.textTooltip
import ktx.scene2d.vis.addTabContentsTo
import ktx.scene2d.vis.tabbedPane
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visProgressBar
import ktx.scene2d.vis.visTable
import ktx.style.defaultStyle
import ktx.style.get
import ktx.style.tabbedPane
import ktx.style.textTooltip
import ktx.style.visTextButton

fun leftPane() = scene2d.visTable {
    buildCharInfo().cell(spaceBottom = 10f, padRight = 5f, growX = true, row = true)

    skin.visTextButton("smallText", "toggle") {
        font = skin["small-font"]
    }
    skin.tabbedPane("smallText", "default") {
        buttonStyle = skin["smallText"]
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
    visTable {
        visImage(Texture(Gdx.files.internal("blueArrow-tp.png"))).cell(row = true)

        progressBarStyleHeight(xpBarStyleName, 10f)

        // TODO: From XP/level system
        stack {
            visProgressBar(0f, 10f, style = xpBarStyleName) {
                value = 4f
                height = 10f
            }
            visLabel("XP: 4/10", "small") {
                setFontScale(0.5f)
                setAlignment(Align.center)
            }
        }.cell(row = true, width = 50f, space = 5f)

        scaledLabel("Level: 1").cell(row = true, expand = true, align = Align.center)
        scaledLabel("Ranger").cell(row = true, expand = true, align = Align.center)
        scaledLabel("Fighter").cell(row = true, expand = true, align = Align.center)
    }.cell(grow = true)

    visTable {
        runState.hero.primaryAttrs.all.forEach { attr ->
            visLabel(attr.type.displayName) {
                textTooltip("Hello, I am a tooltip!") { tt ->
                    tt.setInstant(true)
                    tt.setStyle(VisUI.getSkin().textTooltip("smallerTooltip", extend = defaultStyle) {
                        label = VisUI.getSkin()["small"]
                    })
                }
            }.cell(align = Align.left, expandX = true)
            visLabel(attr.displayValue()).cell(align = Align.center, row = true)
        }
    }.cell(grow = true)
}

@Scene2dDsl
fun KWidget<*>.scaledLabel(text: String) = visLabel(text, "small") {
    setFontScale(0.75f)
}
