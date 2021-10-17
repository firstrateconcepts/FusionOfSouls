package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.configureEntity
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.unit.TargetComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.aliveUnitFamily
import net.firstrateconcepts.fusionofsouls.model.component.unit.hasTarget
import net.firstrateconcepts.fusionofsouls.model.component.unit.target
import net.firstrateconcepts.fusionofsouls.model.component.unit.unitInfo
import net.firstrateconcepts.fusionofsouls.model.event.TargetChangedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.TargetAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.UnitInteractionService
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.service.unit.action.actionProcessor
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.with
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class TargetingSystem(
    private val engine: AsyncPooledEngine,
    private val eventBus: EventBus,
    private val actionQueueBus: ActionQueueBus,
    private val interactionService: UnitInteractionService
) : IteratingSystem(aliveUnitFamily) {
    private val logger = fosLogger()

    private val actionHandler = actionProcessor<TargetAction> { entity, action ->
        val newTarget = action.newTarget
        logger.info { "Unit(${entity.id} | ${entity.name}) changing target to [$newTarget]" }
        val previousTarget = entity.target
        engine.configureEntity(entity) { with<TargetComponent>(newTarget) }
        eventBus.enqueueEventSync(TargetChangedEvent(entity.id, previousTarget, newTarget))
    }

    init {
        engine.addSystem(this)
        actionQueueBus.registerProcessor(actionHandler)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // If entity has a target and can attack said target, no reason to try to switch targets
        if (entity.hasTarget && interactionService.canEntityAttack(entity)) return

        val unitTeam = entity.unitInfo.team
        val closestTarget = entities
            .filter { it.unitInfo.team != unitTeam }
            .minByOrNull { it.currentPosition.dst(entity.currentPosition) }?.id

        if (closestTarget != null && closestTarget != entity.target) {
            actionQueueBus.addAction(TargetAction(entity.id, closestTarget))
        }
    }
}
