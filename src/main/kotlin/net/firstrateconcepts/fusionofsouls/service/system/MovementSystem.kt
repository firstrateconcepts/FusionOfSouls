package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.position
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

val movementFamily = oneOf(PositionComponent::class).get()!!

class MovementSystem(private val engine: AsyncPooledEngine) : IteratingSystem(movementFamily) {
    init {
        engine.addSystem(this)
    }

    @HandlesEvent
    fun battleComplete(runStatusChanged: RunStatusChanged) {
        if (runStatusChanged.newStatus != BattleStatus.AFTER_BATTLE) return

        engine.runOnEngineThread {
            engine.getEntitiesFor(movementFamily).forEach { entity ->
                entity.position.apply {
                    currentPosition.set(initialPosition)
                }
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.currentPosition.apply {
            x += 0.05f
            y += 0.01f
        }
    }
}
