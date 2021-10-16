package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import net.firstrateconcepts.fusionofsouls.model.component.ability
import net.firstrateconcepts.fusionofsouls.model.component.abilityTimer
import net.firstrateconcepts.fusionofsouls.model.component.aliveUnitFamily
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.cooldownReduction
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitAbilityAnimationComplete
import net.firstrateconcepts.fusionofsouls.model.event.UnitUsingAbilityEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.AbilityAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.AbilityService
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.service.unit.action.actionProcessor
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class AbilitySystem(
    private val engine: AsyncPooledEngine,
    private val eventBus: EventBus,
    private val actionQueueBus: ActionQueueBus,
    private val abilityService: AbilityService
) : IteratingSystem(aliveUnitFamily) {
    private val logger = fosLogger()

    private val abilityProcessor = actionProcessor<AbilityAction> { entity, _ ->
        eventBus.enqueueEventSync(UnitUsingAbilityEvent(entity.id))
    }

    init {
        engine.addSystem(this)
        actionQueueBus.registerProcessor(abilityProcessor)
    }

    @HandlesEvent
    fun updateAbilityTimer(event: AttributesChangedEvent) = engine.withUnit(event.unitId) { e -> e.abilityTimer.targetTime = e.ability.cooldown / e.attrs.cooldownReduction() }

    @HandlesEvent
    fun processAbility(event: UnitAbilityAnimationComplete) = engine.withUnit(event.unitId) { entity ->
        logger.info { "Processing ability for ${entity.id}" }
        abilityService.processEntityAbility(entity)
        entity.abilityTimer.reset()
        entity.abilityTimer.resume()
    }

    // TODO: Ability "can use" likely needs to be a strategy pattern based on things like "InRangeStrategy" or "AllyInRangeStrategy"
    //  Might just work as "has valid targets" based on AbilityService#findTargets
    
    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (entity.abilityTimer.isReady) {
            actionQueueBus.addAction(AbilityAction(entity.id))
            entity.abilityTimer.pause()
        }
    }
}
