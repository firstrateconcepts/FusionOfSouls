package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import net.firstrateconcepts.fusionofsouls.model.component.unitFamily
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class AbilitySystem(engine: AsyncPooledEngine) : IteratingSystem(unitFamily) {
    init {
        engine.addSystem(this)
    }

    @HandlesEvent
    fun updateAbilityTimer(event: AttributesChangedEvent) {
        engine.findById(event.unitId)?.apply {
//            abilityTimer.targetTime = <ability base cooldown / CDR>
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) = Unit
}
