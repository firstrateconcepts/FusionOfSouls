package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.util.framework.event.Event

interface UnitEvent : Event { val unitId: Int }
class UnitActivatedEvent(override val unitId: Int) : UnitEvent
class UnitDeactivatedEvent(override val unitId: Int) : UnitEvent
class AttributeRecalculateNeededEvent(override val unitId: Int) : UnitEvent
class AttributesChangedEvent(override val unitId: Int) : UnitEvent
class TargetChangedEvent(override val unitId: Int, val previousTarget: Int?, val newTarget: Int?) : UnitEvent
class UnitAttackingEvent(override val unitId: Int) : UnitEvent
