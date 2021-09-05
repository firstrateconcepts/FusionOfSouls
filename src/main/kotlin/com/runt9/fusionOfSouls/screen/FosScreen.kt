package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.util.render
import ktx.app.KtxScreen

interface FosScreen : KtxScreen {
    val stage: Stage

    override fun render(delta: Float) {
        stage.render(delta)
    }

    override fun hide() {
        stage.clear()
    }
}
