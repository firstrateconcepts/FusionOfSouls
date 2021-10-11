package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import net.firstrateconcepts.fusionofsouls.model.component.attackSpeed
import net.firstrateconcepts.fusionofsouls.model.component.attackTimer
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.unitFamily
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.AttackAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.AttackService
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.service.unit.action.actionProcessor
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class AttackingSystem(
    engine: AsyncPooledEngine,
    private val actionQueueBus: ActionQueueBus,
    private val attackService: AttackService
) : IteratingSystem(unitFamily) {
    private val logger = fosLogger()
    private val attackProcessor = actionProcessor<AttackAction> { entity, action ->
        logger.info { "Processing attack for ${entity.id}" }
        entity.attackTimer.reset()
        entity.attackTimer.resume()
    }

    init {
        engine.addSystem(this)
        actionQueueBus.registerProcessor(attackProcessor)
    }

    @HandlesEvent
    fun updateAttackTimer(event: AttributesChangedEvent) = engine.findById(event.unitId)?.apply { attackTimer.targetTime = 1f / attrs.attackSpeed() }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (entity.attackTimer.isReady && attackService.canEntityAttack(entity)) {
            actionQueueBus.addAction(AttackAction(entity.id))
            entity.attackTimer.pause()
        }
    }
}
