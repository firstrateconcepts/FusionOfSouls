package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Controller

interface FosScreen : KtxScreen {
    val stages: List<FosStage>
    val game: FusionOfSoulsGame

    override fun render(delta: Float) = stages.forEach { it.render(delta) }
    override fun hide() = stages.forEach(Stage::clear)

    fun destroy() {
        game.removeScreen(this::class.java)
        dispose()
    }

    override fun dispose() {
        stages.forEach(Disposable::dispose)
    }
}

abstract class UiScreen : FosScreen {
    protected val uiStage = FosStage()
    protected abstract val uiController: Controller
    override val stages = listOf(uiStage)
    override val game = inject<FusionOfSoulsGame>()
    protected val input = inject<InputMultiplexer>()

    override fun show() {
        input.addProcessor(uiStage)
        uiStage.attachController(uiController)
    }

    override fun hide() {
        input.removeProcessor(uiStage)
        uiStage.detachController()
    }

    override fun render(delta: Float) {
        uiController.render(delta)
        super.render(delta)
    }
}

abstract class GameScreen(worldWidth: Float, worldHeight: Float) : UiScreen() {
    protected val gameStage = FosStage(FitViewport(worldWidth, worldHeight))
    protected abstract val gameController: Controller
    override val stages = listOf(gameStage, uiStage)

    override fun show() {
        gameStage.attachController(gameController)
        input.addProcessor(gameStage)
        super.show()
    }

    override fun hide() {
        gameStage.detachController()
        input.removeProcessor(gameStage)
        super.hide()
    }

    override fun render(delta: Float) {
        gameController.render(delta)
        super.render(delta)
    }
}

inline fun <reified T : FosScreen> FosScreen.changeScreen() = game.setScreen<T>()
