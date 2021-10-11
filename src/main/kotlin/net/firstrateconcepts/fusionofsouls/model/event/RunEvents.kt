package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunStateUpdated(val newState: RunState) : Event
class NewBattleEvent : Event
class BattleStartedEvent : Event
class BattleCompletedEvent : Event
fun EventBus.newBattle() = enqueueEventSync(NewBattleEvent())
fun EventBus.battleStarted() = enqueueEventSync(BattleStartedEvent())
fun EventBus.battleComplete() = enqueueEventSync(BattleCompletedEvent())

class GamePauseChanged(val isPaused: Boolean) : Event
fun EventBus.pauseGame() = enqueueEventSync(GamePauseChanged(true))
fun EventBus.resumeGame() = enqueueEventSync(GamePauseChanged(false))
