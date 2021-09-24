package com.runt9.fusionOfSouls.view.duringRun

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisDialog
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.screen.DuringRunScreen
import com.runt9.fusionOfSouls.screen.MainMenuScreen
import com.runt9.fusionOfSouls.screen.SettingsDialog
import com.runt9.fusionOfSouls.viewportHeight
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton

class InGameMenuDialog(private val game: FosGame, private val settingsDialog: SettingsDialog) : VisDialog("Menu", "dialog") {
    init {
        addCloseButton()
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        closeOnEscape()

        contentTable.apply {
            add(scene2d.visTable(true) {
                visTextButton("Resume") {
                    onClick {
                        hide()
                    }
                }.cell(row = true)

                visTextButton("Settings") {
                    onClick {
                        settingsDialog.show(stage)
                    }
                }.cell(row = true)

                visTextButton("Exit to Main Menu") {
                    onClick {
                        game.reset()
                        game.setScreen<MainMenuScreen>()
                    }
                }.cell(row = true)

                visTextButton("Exit Game") {
                    onClick {
                        Gdx.app.exit()
                    }
                }.cell(row = true)
            }).grow()
        }
    }

    override fun getPrefWidth() = battleWidth * 0.33f
    override fun getPrefHeight() = viewportHeight * 0.6f

    // TODO: These probably should do something
    override fun show(stage: Stage?): VisDialog {
        game.getScreen<DuringRunScreen>().pause()
        return super.show(stage)
    }

    override fun hide() {
        game.getScreen<DuringRunScreen>().resume()
        super.hide()
    }
}
