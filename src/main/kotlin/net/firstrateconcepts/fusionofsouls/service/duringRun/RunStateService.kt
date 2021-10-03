package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunStateService(private val eventBus: EventBus) : RunService() {
    private var runState: RunState? = null

    @HandlesEvent
    fun runStatusChanged(event: RunStatusChanged) = runOnServiceThread {
        runState?.status = event.newStatus
    }

    fun load(): RunState {
        if (runState == null) {
            runState = RunState()
        }

        return runState!!.copy()
    }

    fun save(runState: RunState) = runOnServiceThread {
        if (runState != this@RunStateService.runState) {
            this@RunStateService.runState = runState
            eventBus.enqueueEvent(RunStateUpdated(runState.copy()))
            // TODO: This should also flush the current state to disk
        }
    }

    override fun stopInternal() {
        runState = null
    }
}
