package net.firstrateconcepts.fusionofsouls.view.duringRun

import com.badlogic.gdx.ai.Timepiece
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.event.GamePauseChanged
import net.firstrateconcepts.fusionofsouls.model.event.NewBattleEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunInitializer
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.GameScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController

class DuringRunScreen(
    override val gameController: DuringRunGameController,
    override val uiController: DuringRunUiController,
    private val engine: AsyncPooledEngine,
    private val runInitializer: RunInitializer,
    private val eventBus: EventBus,
    private val aiTimepiece: Timepiece
) : GameScreen(GAME_AREA_WIDTH, GAME_AREA_HEIGHT) {
    private val logger = fosLogger()
    private var isRunning = false
    private var isPaused = false

    @HandlesEvent(NewBattleEvent::class) suspend fun newBattle() = onRenderingThread { isRunning = false }
    @HandlesEvent(BattleStartedEvent::class) suspend fun battleStart() = onRenderingThread { isRunning = true }
    @HandlesEvent(BattleCompletedEvent::class) suspend fun battleComplete() = onRenderingThread {
        logger.info { "Battle ended, pausing render" }
        isRunning = false
    }

    @HandlesEvent
    suspend fun pauseResume(event: GamePauseChanged) = onRenderingThread { isPaused = event.isPaused }

    override fun show() {
        eventBus.registerHandlers(this)
        runInitializer.initialize()
        super.show()
    }

    override fun render(delta: Float) {
        if (isRunning && !isPaused) {
            aiTimepiece.update(delta)
            engine.update(delta)
        }
        super.render(delta)
    }

    override fun hide() {
        eventBus.unregisterHandlers(this)
        runInitializer.dispose()
        isRunning = false
        isPaused = false
        super.hide()
    }
}
