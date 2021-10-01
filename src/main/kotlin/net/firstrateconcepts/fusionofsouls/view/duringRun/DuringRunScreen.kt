package net.firstrateconcepts.fusionofsouls.view.duringRun

import com.badlogic.ashley.core.PooledEngine
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.service.RunInitializer
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.GameScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController

class DuringRunScreen(
    override val gameController: DuringRunGameController,
    override val uiController: DuringRunUiController,
    private val engine: PooledEngine,
    private val runInitializer: RunInitializer
) : GameScreen(GAME_WIDTH, GAME_HEIGHT) {
    private val asyncContext = newSingleThreadAsyncContext("Engine")

    override fun show() {
        runInitializer.initialize()
        super.show()
    }

    override fun render(delta: Float) {
        asyncContext.executor.submit { engine.update(delta) }
        super.render(delta)
    }

    override fun hide() {
        runInitializer.dispose()
        super.hide()
    }
}
