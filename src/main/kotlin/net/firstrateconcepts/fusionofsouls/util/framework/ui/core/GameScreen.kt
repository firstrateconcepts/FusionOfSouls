package net.firstrateconcepts.fusionofsouls.util.framework.ui.core

import com.badlogic.gdx.utils.viewport.FitViewport
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

abstract class GameScreen(worldWidth: Float, worldHeight: Float) : UiScreen() {
    private val gameStage: FosStage = FosStage(FitViewport(worldWidth, worldHeight))
    override val stages = listOf(gameStage, uiStage)
    abstract val gameController: Controller

    override fun show() {
        input.addProcessor(gameStage)
        gameStage.setView(gameController.view)
        super.show()
    }

    override fun hide() {
        super.hide()
        input.removeProcessor(gameStage)
        gameController.dispose()
    }
}
