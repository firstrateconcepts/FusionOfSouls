package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.actions
import net.firstrateconcepts.fusionofsouls.model.component.effects
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.timerInfo
import net.firstrateconcepts.fusionofsouls.model.unit.action.ActionBlocker
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.model.unit.effect.ActionBlockerEffectStrategy
import net.firstrateconcepts.fusionofsouls.model.unit.effect.Effect
import net.firstrateconcepts.fusionofsouls.model.unit.effect.definition.EffectDefinition
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import kotlin.reflect.KClass

// TODO: Mix strategy + factory for the action types, splitting out the "apply/remove" methods into individual classes
// TODO: Unit test individual strategy classes
class EffectService(eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    private val logger = fosLogger()

    fun addEffect(entity: Entity, effectDef: EffectDefinition, duration: Float) {
        logger.info { "Adding effect ${effectDef.name} to entity [${entity.id}]" }
        val timerId = entity.timerInfo.newTimer(duration)
        val effect = Effect(effectDef, timerId)
        entity.effects.add(effect)
        effectDef.strategies.forEach {
            when(it) {
                is ActionBlockerEffectStrategy -> applyActionBlockers(entity, effect, it.actions)
            }
        }
    }

    fun removeEffect(entity: Entity, effect: Effect) {
        logger.info { "Removing effect ${effect.definition.name} from entity [${entity.id}]" }
        entity.effects.remove(effect)
        effect.definition.strategies.forEach {
            when(it) {
                is ActionBlockerEffectStrategy -> removeActionBlockers(entity, effect)
            }
        }
    }

    private fun applyActionBlockers(entity: Entity, effect: Effect, actions: Array<out KClass<out UnitAction>>) =
        entity.actions.blockers.addAll(actions.map { ActionBlocker(effect, it) })
    private fun removeActionBlockers(entity: Entity, effect: Effect) = entity.actions.blockers.removeIf { it.source == effect }

    // Effects have a strategy of what they do (damage, heal, blocker, attribute modifier)
    // Effect added event, creates timer with effect as source, and applies the strategy
    // System tick will check for finished timer, remove timer, un-apply strategy
}
