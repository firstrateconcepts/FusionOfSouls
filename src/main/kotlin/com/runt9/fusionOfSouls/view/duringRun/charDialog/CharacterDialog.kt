package com.runt9.fusionOfSouls.view.duringRun.charDialog

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
        addCloseButton()
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        button("Done")

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

    override fun getPrefWidth() = (battleWidth * 0.75).toFloat()
    override fun getPrefHeight() = viewportHeight.toFloat() - resourceBarHeight
}
