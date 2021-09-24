package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisDialog
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visCheckBox
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton

class SettingsDialog(private val game: FosGame, private val settings: Settings) : VisDialog("Settings", "dialog") {
    private val fullscreenCheckbox: VisCheckBox
    private val vsyncCheckbox: VisCheckBox

    init {
        addCloseButton()
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        closeOnEscape()

        contentTable.apply {
            add(scene2d.visTable {
                fullscreenCheckbox = visCheckBox("Fullscreen") {
                    isChecked = settings.fullscreen

                    onChange {
                        if (isChecked) game.setFullscreen() else game.setWindowed()
                    }
                }.cell(row = true)

                vsyncCheckbox = visCheckBox("VSync") {
                    isChecked = settings.vsync

                    onChange {
                        Gdx.graphics.setVSync(isChecked)
                    }
                }.cell(row = true)
            }).grow()
        }

        button(scene2d.visTextButton("Done") {
            onClick {
                settings.fullscreen = fullscreenCheckbox.isChecked
                settings.vsync = vsyncCheckbox.isChecked
                settings.save()
                hide()
            }
        })
    }

    override fun getPrefWidth() = viewportWidth * 0.5f
    override fun getPrefHeight() = viewportHeight * 0.75f
}
