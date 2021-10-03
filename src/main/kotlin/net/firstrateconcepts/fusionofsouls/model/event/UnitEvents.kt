package net.firstrateconcepts.fusionofsouls.model.event

import com.badlogic.gdx.math.Vector2
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import java.util.*

class UnitActivatedEvent(val id: UUID) : Event
class UnitDeactivatedEvent(val id: UUID) : Event
class AttributeRecalculateNeededEvent(val unitId: UUID) : Event
class AttributesChangedEvent(val unitId: UUID) : Event
