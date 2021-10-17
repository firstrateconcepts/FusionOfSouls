package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import net.firstrateconcepts.fusionofsouls.model.component.unit.aliveUnitFamily
import net.firstrateconcepts.fusionofsouls.model.component.attackSpeed
import net.firstrateconcepts.fusionofsouls.model.component.unit.attackTimer
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitAttackAnimationComplete
import net.firstrateconcepts.fusionofsouls.model.event.UnitAttackingEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.AttackAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.UnitInteractionService
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.service.unit.action.actionProcessor
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class AttackingSystem(
    private val engine: AsyncPooledEngine,
    private val eventBus: EventBus,
    private val actionQueueBus: ActionQueueBus,
    private val interactionService: UnitInteractionService
) : IteratingSystem(aliveUnitFamily) {
    private val logger = fosLogger()

    private val attackProcessor = actionProcessor<AttackAction> { entity, _ ->
        eventBus.enqueueEventSync(UnitAttackingEvent(entity.id))
    }

    init {
        engine.addSystem(this)
        actionQueueBus.registerProcessor(attackProcessor)
    }

    @HandlesEvent
    fun updateAttackTimer(event: AttributesChangedEvent) = engine.withUnit(event.unitId) { it.attackTimer.targetTime = 1f / it.attrs.attackSpeed() }

    @HandlesEvent
    fun processAttack(event: UnitAttackAnimationComplete) = engine.withUnit(event.unitId) { entity ->
        logger.info { "Processing attack for ${entity.id}" }
        interactionService.processEntityAttack(entity)
        entity.attackTimer.reset()
        entity.attackTimer.resume()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (entity.attackTimer.isReady && interactionService.canEntityAttack(entity)) {
            actionQueueBus.addAction(AttackAction(entity.id))
            entity.attackTimer.pause()
        }
    }
}
