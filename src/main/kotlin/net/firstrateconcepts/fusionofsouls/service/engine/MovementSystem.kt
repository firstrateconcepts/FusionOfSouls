package net.firstrateconcepts.fusionofsouls.service.engine

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.event.UnitPositionUpdatedEvent
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class MovementSystem(private val eventBus: EventBus) : IteratingSystem(oneOf(PositionComponent::class).get()) {
    val positionMapper = mapperFor<PositionComponent>()
    val idMapper = mapperFor<IdComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        positionMapper.get(entity).apply {
            position.x += 0.01f
            position.y += 0.005f
            eventBus.enqueueEventSync(UnitPositionUpdatedEvent(idMapper.get(entity).id, position.cpy()))
        }
    }
}
