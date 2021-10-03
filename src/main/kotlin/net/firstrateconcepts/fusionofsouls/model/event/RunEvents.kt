package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.RunStatus
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunStateUpdated(val newState: RunState) : Event
class RunStatusChanged(val newStatus: RunStatus) : Event
fun EventBus.changeRunStatus(status: RunStatus) = enqueueEventSync(RunStatusChanged(status))
