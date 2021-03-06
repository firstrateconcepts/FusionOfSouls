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
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitActionType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.AttackService
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.with
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class TargetingSystem(
    private val engine: AsyncPooledEngine,
    private val eventBus: EventBus,
    private val actionQueueBus: ActionQueueBus,
    private val attackService: AttackService
) : IteratingSystem(aliveUnitFamily) {
    private val logger = fosLogger()

    init {
        engine.addSystem(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // If entity has a target and can attack said target, no reason to try to switch targets
        if (entity.hasTarget && attackService.canEntityAttack(entity)) return

        val unitTeam = entity.unitInfo.team
        val closestTarget = entities
            .filter { it.unitInfo.team != unitTeam }
            .minByOrNull { it.currentPosition.dst(entity.currentPosition) }?.id

        if (closestTarget != null && closestTarget != entity.target) {
            actionQueueBus.addAction(entity, UnitActionType.TARGET) {
                logger.info { "Unit(${entity.id} | ${entity.name}) changing target to [$closestTarget]" }
                val previousTarget = entity.target
                engine.configureEntity(entity) { with<TargetComponent>(closestTarget) }
                eventBus.enqueueEventSync(TargetChangedEvent(entity.id, previousTarget, closestTarget))
            }
        }
    }
}
