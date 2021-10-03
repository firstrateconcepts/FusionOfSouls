package net.firstrateconcepts.fusionofsouls.view.duringRun

import com.badlogic.ashley.core.PooledEngine
import ktx.async.newSingleThreadAsyncContext
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.event.GamePauseChanged
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunInitializer
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.GameScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController

class DuringRunScreen(
    override val gameController: DuringRunGameController,
    override val uiController: DuringRunUiController,
    private val engine: PooledEngine,
    private val runInitializer: RunInitializer,
    private val eventBus: EventBus
) : GameScreen(GAME_AREA_WIDTH, GAME_AREA_HEIGHT) {
    private val asyncContext = newSingleThreadAsyncContext("Engine")
    private var isRunning = false
    private var isPaused = false

    @HandlesEvent
    suspend fun statusChanged(event: RunStatusChanged) = onRenderingThread { isRunning = event.newStatus == BattleStatus.DURING_BATTLE }

    @HandlesEvent
    suspend fun pauseResume(event: GamePauseChanged) = onRenderingThread { isPaused = event.isPaused }

    override fun show() {
        eventBus.registerHandlers(this)
        runInitializer.initialize()
        super.show()
    }

    override fun render(delta: Float) {
        if (isRunning && !isPaused) asyncContext.executor.submit { engine.update(delta) }
        super.render(delta)
    }

    override fun hide() {
        eventBus.unregisterHandlers(this)
        runInitializer.dispose()
        super.hide()
    }
}
