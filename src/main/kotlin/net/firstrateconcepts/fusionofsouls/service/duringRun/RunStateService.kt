package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class RunStateService(override val eventBus: EventBus) : RunService() {
    private val logger = fosLogger()
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

    fun save(runState: RunState) {
        if (runState != this.runState) {
            logger.info { "Saving run state" }
            this.runState = runState
            eventBus.enqueueEventSync(RunStateUpdated(runState.copy()))
            // TODO: This should also flush the current state to disk
        }
    }

    override fun stopInternal() {
        runState = null
    }
}
