package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.event.NewBattleEvent
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class RunStateService(private val eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    private val logger = fosLogger()
    private var runState: RunState? = null

    @HandlesEvent(NewBattleEvent::class) fun newBattle() { runState?.status = BattleStatus.BEFORE_BATTLE }
    @HandlesEvent(BattleStartedEvent::class) fun battleStarted() { runState?.status = BattleStatus.DURING_BATTLE }
    @HandlesEvent(BattleCompletedEvent::class) fun battleCompleted() { runState?.status = BattleStatus.AFTER_BATTLE }

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
