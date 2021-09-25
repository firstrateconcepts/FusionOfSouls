package com.runt9.fusionOfSouls.view.duringRun

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable
import com.runt9.fusionOfSouls.basicMargin
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.service.BattleStatus
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.observableLabel
import com.runt9.fusionOfSouls.util.rectPixmapTexture
import com.runt9.fusionOfSouls.util.toDrawable
import com.runt9.fusionOfSouls.view.duringRun.charDialog.CharacterDialog
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton

class TopBar(private val inGameMenuDialog: InGameMenuDialog) : VisTable(true), KTable {
    init {
        defaults().expand().left().padLeft(basicMargin.toFloat())
        y = viewportHeight - resourceBarHeight.toFloat()
        setSize(viewportWidth.toFloat(), resourceBarHeight.toFloat())
        background(rectPixmapTexture(viewportWidth, resourceBarHeight, Color.SLATE).toDrawable())

        visTable(true) {
            defaults().expand().center()
            observableLabel(runState.goldListeners) { "Gold: ${runState.gold}" }
            observableLabel(runState.activeUnitListeners) { "Units: ${runState.activeUnits.size} / ${runState.unitCap}" }
        }

        defaults().expand().right().padRight(basicMargin.toFloat())
        visTable(true) {
            defaults().expand().center()
            observableLabel(runState.roomNumberListeners) { "Room ${runState.floor}:${runState.room}" }

            visTextButton("Hero") {
                setOrigin(Align.center)
                scaleBy(-0.33f)
                isTransform = true
                onClick {
                    if (isDisabled) return@onClick
                    CharacterDialog(runState.hero.name).show(stage)
                }

                runState.statusListeners += { isDisabled = it == BattleStatus.DURING }
            }

            visTextButton("Boss") {
                setOrigin(Align.center)
                scaleBy(-0.33f)
                isTransform = true
                onClick {
                    if (isDisabled) return@onClick
                    BossDialog().show(stage)
                }

                runState.statusListeners += { isDisabled = it == BattleStatus.DURING }
            }

            visTextButton("Menu") {
                setOrigin(Align.center)
                scaleBy(-0.33f)
                isTransform = true
                onClick {
                    this@TopBar.inGameMenuDialog.show(stage)
                }
            }
        }
    }
}
