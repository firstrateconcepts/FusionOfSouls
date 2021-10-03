package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunStateUpdated(val newState: RunState) : Event
class RunStatusChanged(val newStatus: BattleStatus) : Event
fun EventBus.changeRunStatus(status: BattleStatus) = enqueueEventSync(RunStatusChanged(status))

class GamePauseChanged(val isPaused: Boolean) : Event
fun EventBus.pauseGame() = enqueueEventSync(GamePauseChanged(true))
fun EventBus.resumeGame() = enqueueEventSync(GamePauseChanged(false))
