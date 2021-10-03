package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import net.firstrateconcepts.fusionofsouls.model.RunStatus
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.model.event.changeRunStatus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.eventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class DuringRunUiController(private val eventBus: EventBus) : Controller {
    override val vm = DuringRunUiViewModel()
    override val view = DuringRunUiView(this, vm)

    private val runStatusHandler = eventHandler<RunStatusChanged> {
        vm.isStartBattle(it.newStatus == RunStatus.BEFORE_BATTLE)
    }

    override fun load() {
        eventBus.registerHandler(runStatusHandler)
        super.load()
    }

    fun startBattle() {
        eventBus.changeRunStatus(RunStatus.DURING_BATTLE)
    }

    override fun dispose() {
        eventBus.deregisterHandler(runStatusHandler)
        super.dispose()
    }
}
