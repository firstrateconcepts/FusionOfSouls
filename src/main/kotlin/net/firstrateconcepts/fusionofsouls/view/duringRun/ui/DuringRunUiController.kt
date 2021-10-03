package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.model.event.changeRunStatus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class DuringRunUiController(private val eventBus: EventBus) : Controller {
    override val vm = DuringRunUiViewModel()
    override val view = DuringRunUiView(this, vm)

    @HandlesEvent
    suspend fun runStatusHandler(event: RunStatusChanged) = onRenderingThread {
        vm.isStartBattle(event.newStatus == BattleStatus.BEFORE_BATTLE)
    }

    override fun load() {
        eventBus.registerHandlers(this)
        super.load()
    }

    fun startBattle() {
        eventBus.changeRunStatus(BattleStatus.DURING_BATTLE)
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
