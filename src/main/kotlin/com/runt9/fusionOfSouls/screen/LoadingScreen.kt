package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.FosGame
import ktx.actors.centerPosition
import ktx.app.KtxScreen
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel

class LoadingScreen(private val game: FosGame, private val assets: AssetManager, private val stage: Stage) : KtxScreen {
    override fun show() {
        stage.clear()

        stage.actors {
            visLabel("Fusion of Souls") {
                centerPosition()
            }
        }
    }

    override fun render(delta: Float) {
        assets.update()
        stage.viewport.apply()
        stage.act(delta)
        stage.draw()

//        stage.batch.use {
//            font.draw(it, "Fusion of Souls", 100f, 150f)
//            if (assets.isFinished) {
//                font.draw(it, "Click to continue", 100f, 100f)
//            } else {
//                font.draw(it, "Loading Assets", 100f, 100f)
//            }
//        }

        if (Gdx.input.isTouched && assets.isFinished) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<MainMenuScreen>()
        }
    }
}
