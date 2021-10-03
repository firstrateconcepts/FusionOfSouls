package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.FosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

abstract class EventPostingEntityListener : EntityListener, RunService() {
    abstract val logger: FosLogger
    abstract val eventBus: EventBus
    abstract val engine: AsyncPooledEngine
    abstract val family: Family

    override fun startInternal() {
        engine.addEntityListener(family, this)
    }

    override fun stopInternal() {
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        runOnServiceThread {
            val event = entityAddedEvent(entity.id)
            logger.info { "Entity [${entity.id}] added, posting ${event.name}" }
            eventBus.enqueueEvent(event)
        }
    }

    override fun entityRemoved(entity: Entity) {
        runOnServiceThread {
            val event = entityRemovedEvent(entity.id)
            logger.info { "Entity [${entity.id}] removed, posting ${event.name}" }
            eventBus.enqueueEvent(event)
        }
    }

    abstract fun entityAddedEvent(id: Int): Event
    abstract fun entityRemovedEvent(id: Int): Event
}
