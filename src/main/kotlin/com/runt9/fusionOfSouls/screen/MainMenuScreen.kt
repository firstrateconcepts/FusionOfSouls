package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.model.Settings
import ktx.app.KtxScreen
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel

class MainMenuScreen(private val settings: Settings, private val stage: Stage) : KtxScreen {
    override fun show() {
        stage.clear()
        stage.actors { visLabel("Fusion of Souls") }
    }

    override fun render(delta: Float) {
        stage.viewport.apply()
        stage.act(delta)
        stage.draw()
    }
}
