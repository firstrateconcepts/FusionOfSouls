package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.config.inject

interface FosScreen : KtxScreen {
    val stages: List<FosStage>
    val game: FusionOfSoulsGame

    override fun render(delta: Float) = stages.forEach { it.render(delta) }
    override fun hide() = stages.forEach(Stage::clear)

    fun destroy() {
        game.removeScreen(this::class.java)
        dispose()
    }
}

abstract class UiScreen : FosScreen {
    // TODO: viewport configuration
    protected val uiStage = FosStage()
    override val stages = listOf(uiStage)
    override val game = inject<FusionOfSoulsGame>()
}

abstract class GameScreen : UiScreen() {
    // TODO: viewport configuration
    protected val gameStage = FosStage()
    override val stages = listOf(gameStage, uiStage)
}

inline fun <reified T : FosScreen> FosScreen.changeScreen() {
    game.setScreen<T>()
}
