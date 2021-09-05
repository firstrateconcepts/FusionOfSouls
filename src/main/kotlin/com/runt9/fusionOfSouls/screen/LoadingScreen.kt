package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.FosGame
import ktx.actors.centerPosition
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel

class LoadingScreen(private val game: FosGame, private val assets: AssetManager, override val stage: Stage) : FosScreen {
    override fun show() {
        stage.clear()

        stage.actors {
            visLabel("Loading...") {
                centerPosition()
            }
        }
    }

    override fun render(delta: Float) {
        assets.update()

        if (assets.isFinished/* && Gdx.input.isTouched*/) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<MainMenuScreen>()
        }

        super.render(delta)
    }
}
