package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import net.firstrateconcepts.fusionofsouls.model.component.aliveUnitFamily
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class AbilitySystem(private val engine: AsyncPooledEngine) : IteratingSystem(aliveUnitFamily) {
    init {
        engine.addSystem(this)
    }

    @HandlesEvent
    fun updateAbilityTimer(event: AttributesChangedEvent) {
        engine.withUnit(event.unitId) {
//            abilityTimer.targetTime = <ability base cooldown / CDR>
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) = Unit
}
