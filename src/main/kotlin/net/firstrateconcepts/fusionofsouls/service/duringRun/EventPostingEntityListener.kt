package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.util.ext.FosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import java.util.*

interface EventPostingEntityListener : EntityListener, RunService {
    val logger: FosLogger
    val eventBus: EventBus
    val engine: PooledEngine
    val family: Family

    override fun start() {
        engine.addEntityListener(family, this)
    }

    override fun stop() {
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        val event = entityAddedEvent(entity.id)
        logger.info { "Entity [${entity.id}] added, posting ${event.name}" }
        eventBus.enqueueEventSync(event)
    }

    override fun entityRemoved(entity: Entity) {
        val event = entityRemovedEvent(entity.id)
        logger.info { "Entity [${entity.id}] removed, posting ${event.name}" }
        eventBus.enqueueEventSync(event)
    }

    fun entityAddedEvent(id: UUID): Event
    fun entityRemovedEvent(id: UUID): Event
}
