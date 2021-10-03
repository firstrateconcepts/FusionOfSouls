package net.firstrateconcepts.fusionofsouls.view.duringRun

import com.badlogic.ashley.core.PooledEngine
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.model.RunStatus
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunInitializer
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.eventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.GameScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController

class DuringRunScreen(
    override val gameController: DuringRunGameController,
    override val uiController: DuringRunUiController,
    private val engine: PooledEngine,
    private val runInitializer: RunInitializer,
    private val eventBus: EventBus
) : GameScreen(GAME_WIDTH, GAME_HEIGHT) {
    private val asyncContext = newSingleThreadAsyncContext("Engine")
    private var isRunning = false

    private val runStatusHandler = eventHandler<RunStatusChanged> {
        isRunning = it.newStatus == RunStatus.DURING_BATTLE
    }

    override fun show() {
        eventBus.registerHandler(runStatusHandler)
        runInitializer.initialize()
        super.show()
    }

    override fun render(delta: Float) {
        if (isRunning) asyncContext.executor.submit { engine.update(delta) }
        super.render(delta)
    }

    override fun hide() {
        eventBus.deregisterHandler(runStatusHandler)
        runInitializer.dispose()
        super.hide()
    }
}
