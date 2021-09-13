package com.runt9.fusionOfSouls.screen.duringRun.charDialog

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisDialog
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.viewportHeight
import ktx.scene2d.KTable
import ktx.scene2d.scene2d
import ktx.scene2d.splitPane

class CharacterDialog(title: String) : VisDialog(title, "dialog"), KTable {
    init {
        TooltipManager.getInstance().apply {
            instant()
            animations = false
        }
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        button("Done")
        key(Input.Keys.ESCAPE, null)

        contentTable.apply {
            val pane = scene2d.splitPane {
                maxSplitAmount = 0.5f
                minSplitAmount = 0.5f
                setFirstWidget(leftPane())
                setSecondWidget(rightPane())
            }

            add(pane).grow().top()
        }
    }

    override fun getPrefWidth(): Float {
        return (battleWidth * 0.75).toFloat()
    }

    override fun getPrefHeight(): Float {
        return viewportHeight.toFloat() - resourceBarHeight
    }
    
}
