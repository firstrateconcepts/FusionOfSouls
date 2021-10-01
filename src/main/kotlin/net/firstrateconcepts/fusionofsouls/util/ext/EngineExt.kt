package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.idFamily
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import java.util.*

fun Engine.findById(id: UUID) = getEntitiesFor(idFamily).find { it.id == id }

fun EventBus.entityToEventListener(addedEventBuilder: (Entity) -> Event, removedEventBuilder: (Entity) -> Event) = object : EntityListener {
    override fun entityAdded(entity: Entity) = enqueueEventSync(addedEventBuilder(entity))
    override fun entityRemoved(entity: Entity) = enqueueEventSync(removedEventBuilder(entity))
}

