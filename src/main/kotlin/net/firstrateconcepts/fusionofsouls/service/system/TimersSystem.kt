package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import net.firstrateconcepts.fusionofsouls.model.component.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.TimersComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.timers
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

val timersFamily = allOf(TimersComponent::class, AliveComponent::class).get()!!

class TimersSystem(private val engine: AsyncPooledEngine) : IteratingSystem(timersFamily) {
    private val logger = fosLogger()
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

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleComplete() = engine.runOnEngineThread {
        logger.info { "Battle ended, resetting timers" }
        engine.getEntitiesFor(timersFamily).forEach { entity ->
            entity.timers.values.forEach { it.reset(false) }
            timerCallbacks[entity.id]?.invoke(entity)
        }
    }
}
