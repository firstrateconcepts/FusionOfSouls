package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.util.fosVisTable
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton

class MainMenuScreen(private val game: FosGame, private val settingsDialog: SettingsDialog, override val stage: Stage) : FosScreen {
    override fun show() {
        stage.clear()
        stage.actors {
            fosVisTable {
                visLabel("Fusion of Souls") {
                    setAlignment(Align.center)
                }

                row()

                visTextButton("New Run") {
                    onClick {
                        game.setScreen<RunStartScreen>()
                    }
                }

                row()

                visTextButton("Settings") {
                    onClick {
                        settingsDialog.show(stage)
                    }
                }

                row()

                visTextButton("Quit") {
                    onClick {
                        Gdx.app.exit()
                    }
                }
            }
        }
    }
}
