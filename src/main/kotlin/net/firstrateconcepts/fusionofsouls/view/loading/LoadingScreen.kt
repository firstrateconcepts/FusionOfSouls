package net.firstrateconcepts.fusionofsouls.view.loading

import ktx.actors.centerPosition
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.util.ext.FosStage
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen

class LoadingScreen(private val game: FusionOfSoulsGame, private val assets: AssetStorage, private val assetLoader: AssetLoader) : KtxScreen {
    private val tempStage = FosStage()
    private var loadingComplete = false

    override fun show() {
        tempStage.actors {
            visLabel("Loading...") {
                centerPosition()
            }
        }

        assetLoader.load().invokeOnCompletion {
            loadingComplete = true
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<MainMenuScreen>()
        }
    }

    override fun render(delta: Float) {
        if (!loadingComplete) {
            assets.progress.run {
                println("$loaded / $total (${percent * 100})")
            }
        }
        tempStage.render(delta)
    }

    override fun hide() {
        tempStage.clear()
    }
}
