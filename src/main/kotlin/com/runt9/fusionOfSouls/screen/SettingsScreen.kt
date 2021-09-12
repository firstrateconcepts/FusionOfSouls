package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.util.fosVisTable
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.vis.visCheckBox
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton

class SettingsScreen(private val game: FosGame, private val settings: Settings, override val stage: Stage) : FosScreen {
    private lateinit var fullscreenCheckbox: VisCheckBox
    private lateinit var vsyncCheckbox: VisCheckBox

    override fun show() {
        stage.actors {
            fosVisTable {
                visLabel("Settings")

                row()

                fullscreenCheckbox = visCheckBox("Fullscreen") {
                    isChecked = settings.fullscreen

                    onChange {
                        if (isChecked) game.setFullscreen() else game.setWindowed()
                    }
                }

                row()

                vsyncCheckbox = visCheckBox("VSync") {
                    isChecked = settings.vsync

                    onChange {
                        Gdx.graphics.setVSync(isChecked)
                    }
                }

                row()

                visTextButton("Done") {
                    onClick {
                        settings.fullscreen = fullscreenCheckbox.isChecked
                        settings.vsync = vsyncCheckbox.isChecked
                        settings.save()
                        game.setScreen<MainMenuScreen>()
                    }
                }
            }
        }
    }
}
