package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxScreen
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame

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
