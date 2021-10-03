package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.position
import net.firstrateconcepts.fusionofsouls.model.event.UnitPositionUpdatedEvent
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class MovementSystem(
    private val eventBus: EventBus,
    engine: PooledEngine
) : IteratingSystem(oneOf(PositionComponent::class).get()) {
    init {
        engine.addSystem(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.position.apply {
            x += 0.05f
            y += 0.01f
            eventBus.enqueueEventSync(UnitPositionUpdatedEvent(entity.id, this.cpy()))
        }
    }
}
