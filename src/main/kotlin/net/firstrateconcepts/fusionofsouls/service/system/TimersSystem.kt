package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import net.firstrateconcepts.fusionofsouls.model.component.unit.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.TimersComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.timers
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

val timersFamily = allOf(TimersComponent::class, AliveComponent::class).get()!!

class TimersSystem(private val engine: AsyncPooledEngine) : IteratingSystem(timersFamily) {
    private val logger = fosLogger()
    private val timerTickCallbacks = mutableMapOf<Int, Entity.() -> Unit>()
    private val timerReadyCallbacks = mutableMapOf<Int, Entity.() -> Unit>()

    init {
        engine.addSystem(this)
    }

    fun onTimerTick(timerId: Int, callback: Entity.() -> Unit) {
        timerTickCallbacks[timerId] = callback
    }

    fun onTimerReady(timerId: Int, callback: Entity.() -> Unit) {
        timerReadyCallbacks[timerId] = callback
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.timers.forEach { (id, timer) ->
            if (timer.isPaused) return@forEach

            timer.tick(deltaTime)
            timerTickCallbacks[id]?.invoke(entity)
            if (timer.isReady) timerReadyCallbacks[id]?.invoke(entity)
        }
    }

    @HandlesEvent
    fun unitDeactivated(event: UnitDeactivatedEvent) = engine.withUnit(event.unitId) {
        it.timers.keys.forEach { key ->
            timerTickCallbacks.remove(key)
            timerReadyCallbacks.remove(key)
        }
    }

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleComplete() = engine.runOnEngineThread {
        logger.info { "Battle ended, resetting timers" }
        engine.getEntitiesFor(timersFamily).forEach { entity ->
            entity.timers.forEach { (id, timer) ->
                timer.reset(false)
                timerTickCallbacks[id]?.invoke(entity)
            }
        }
    }
}
