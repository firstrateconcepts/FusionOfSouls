package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.TimersComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.timers
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine

val timersFamily = oneOf(TimersComponent::class).get()!!

class TimersSystem(engine: AsyncPooledEngine) : IteratingSystem(timersFamily) {
    private val timerCallbacks = mutableMapOf<Int, Entity.() -> Unit>()

    init {
        engine.addSystem(this)
    }

    fun onTimerTick(unitId: Int, callback: Entity.() -> Unit) { timerCallbacks[unitId] = callback }
    fun removeTimerTickCallback(unitId: Int) = timerCallbacks.remove(unitId)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.timers.values.forEach { it.tick(deltaTime) }
        timerCallbacks[entity.id]?.invoke(entity)
    }
}
