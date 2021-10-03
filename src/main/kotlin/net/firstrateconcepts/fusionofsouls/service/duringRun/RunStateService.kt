package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.eventHandler

class RunStateService(registry: RunServiceRegistry, private val eventBus: EventBus) : RunService {
    private var runState: RunState? = null

    private val runStatusHandler = eventHandler<RunStatusChanged> {
        runState?.status = it.newStatus
    }

    init {
        registry.register(this)
    }

    override fun start() {
        eventBus.registerHandler(runStatusHandler)
    }

    fun load(): RunState {
        if (runState == null) {
            runState = RunState()
        }

        return runState!!.copy()
    }

    fun save(runState: RunState) {
        if (runState == this.runState) return
        this.runState = runState
        eventBus.enqueueEventSync(RunStateUpdated(runState.copy()))
        // TODO: This should also flush the current state to disk
    }

    override fun stop() {
        runState = null
        eventBus.deregisterHandler(runStatusHandler)
    }
}
