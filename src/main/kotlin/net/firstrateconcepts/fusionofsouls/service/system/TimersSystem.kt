package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.TimersComponent
import net.firstrateconcepts.fusionofsouls.model.component.timers
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine

val timersFamily = oneOf(TimersComponent::class).get()!!

class TimersSystem(engine: AsyncPooledEngine) : IteratingSystem(timersFamily) {
    init {
        engine.addSystem(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) = entity.timers.values.forEach { it.tick(deltaTime) }
}
