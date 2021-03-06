package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.unit.interceptors
import net.firstrateconcepts.fusionofsouls.model.component.unit.passives
import net.firstrateconcepts.fusionofsouls.model.loot.Rarity
import net.firstrateconcepts.fusionofsouls.model.loot.passive.InterceptorPassiveStrategy
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition
import net.firstrateconcepts.fusionofsouls.model.loot.passive.Ravenous
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class PassiveService(eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    fun addPassive(entity: Entity, passive: PassiveDefinition) {
        entity.passives.add(passive)
        when (passive.strategy) {
            is InterceptorPassiveStrategy -> addPassiveInterceptors(entity, passive.strategy as InterceptorPassiveStrategy)
        }
    }

    fun removePassive(entity: Entity, passive: PassiveDefinition) {
        entity.passives.remove(passive)
        when (passive.strategy) {
            is InterceptorPassiveStrategy -> removePassiveInterceptors(entity, passive.strategy as InterceptorPassiveStrategy)
        }
    }

    private fun addPassiveInterceptors(entity: Entity, strategy: InterceptorPassiveStrategy) = strategy.interceptors.forEach { (target, interceptor) ->
        entity.interceptors.apply {
            if (target.isUnit) addUnitInterceptor(interceptor)
            if (target.isTarget) addTargetInterceptor(interceptor)
        }
    }

    private fun removePassiveInterceptors(entity: Entity, strategy: InterceptorPassiveStrategy) = strategy.interceptors.forEach { (target, interceptor) ->
        entity.interceptors.apply {
            if (target.isUnit) removeUnitInterceptor(interceptor)
            if (target.isTarget) removeTargetInterceptor(interceptor)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun randomPassive(rarity: Rarity): PassiveDefinition {
        // TODO: Random
        return Ravenous
    }
}
