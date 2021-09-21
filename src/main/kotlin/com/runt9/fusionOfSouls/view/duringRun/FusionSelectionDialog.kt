package com.runt9.fusionOfSouls.view.duringRun

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisDialog
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.viewportHeight
import ktx.actors.onChangeEvent
import ktx.scene2d.KTable
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.visTable

// TODO: Find a good home for this
@Scene2dDsl
fun PopupMenu.fuseMenuItem() = menuItem("Fuse") {
    val disabler = { if (runState.hero.fusions.size >= runState.fusionCap) isDisabled = true }
    disabler()
    runState.hero.fusionAddedListeners += {
        disabler()
    }

    label.style = VisUI.getSkin().get("small", Label.LabelStyle::class.java)
    imageCell.size(0f)

    onChangeEvent { e ->
        FusionSelectionDialog().show(e.stage)
    }
}

class FusionSelectionDialog : VisDialog("Select Fusion", "dialog"), KTable {
    init {
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        isModal = false
        button("Cancel")
        key(Input.Keys.ESCAPE, null)

        contentTable.apply {
            scene2d.visTable(defaultSpacing = true) {

            }
        }
    }

    override fun getPrefWidth() = battleWidth * 0.25f
    override fun getPrefHeight() = viewportHeight * 0.75f
}
