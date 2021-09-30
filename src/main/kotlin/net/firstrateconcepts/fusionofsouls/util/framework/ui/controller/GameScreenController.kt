package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.viewport.FitViewport
import net.firstrateconcepts.fusionofsouls.util.ext.FosStage
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.View

abstract class GameScreenController(worldWidth: Float, worldHeight: Float) : UiScreenController() {
    protected val gameStage = FosStage(FitViewport(worldWidth, worldHeight))
    override val stages = listOf(gameStage, uiStage)
    abstract val gameView: View

    override fun show() {
        input.addProcessor(gameStage)
        gameStage.root = gameView as Group
        super.show()
    }

    override fun hide() {
        input.removeProcessor(gameStage)
        super.hide()
    }
}
