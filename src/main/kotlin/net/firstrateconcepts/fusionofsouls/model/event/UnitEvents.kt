package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.util.framework.event.Event

class UnitActivatedEvent(val id: Int) : Event
class UnitDeactivatedEvent(val id: Int) : Event
class AttributeRecalculateNeededEvent(val unitId: Int) : Event
class AttributesChangedEvent(val unitId: Int) : Event
class TargetChangedEvent(val unitId: Int, val previousTarget: Int?, val newTarget: Int?) : Event
