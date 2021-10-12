package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.hasTarget
import net.firstrateconcepts.fusionofsouls.model.component.isInRangeOfTarget
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class AttackService(override val eventBus: EventBus) : RunService() {
    fun canEntityAttack(entity: Entity): Boolean {
        if (!entity.hasTarget) return false

        return entity.isInRangeOfTarget
    }
}
