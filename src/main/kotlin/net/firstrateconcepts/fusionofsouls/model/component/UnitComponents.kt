package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import kotlinx.coroutines.channels.Channel
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorHook
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorScope
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInteraction
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInterceptor
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.action.ActionBlocker
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.model.unit.effect.Effect
import net.firstrateconcepts.fusionofsouls.util.ext.Timer

// TODO: Break this file up

class UnitComponent(val type: UnitType, val team: UnitTeam, val ability: AbilityDefinition) : Component
val unitFamily = oneOf(UnitComponent::class).get()!!
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!
val Entity.team get() = unitInfo.team
val Entity.ability get() = unitInfo.ability

class PassivesComponent : Component {
    val passives = mutableListOf<PassiveDefinition>()
}
val passivesMapper = mapperFor<PassivesComponent>()
val Entity.passives get() = this[passivesMapper]!!.passives

class InterceptorsComponent : Component {
    val asUnitInterceptors = mutableMapOf<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>()
    val asTargetInterceptors = mutableMapOf<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>()
    
    @Suppress("UNCHECKED_CAST")
    private fun MutableMap<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>.addInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) =
        computeIfAbsent(interceptor.hook) { mutableListOf() }.add(interceptor as UnitInterceptor<UnitInteraction>)

    fun addUnitInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asUnitInterceptors.addInterceptor(interceptor)
    fun addTargetInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asTargetInterceptors.addInterceptor(interceptor)
    fun removeUnitInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asUnitInterceptors[interceptor.hook]?.remove(interceptor)
    fun removeTargetInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asTargetInterceptors[interceptor.hook]?.remove(interceptor)
}
val interceptorsMapper = mapperFor<InterceptorsComponent>()
val Entity.interceptors get() = this[interceptorsMapper]!!

private fun MutableMap<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>.intercept(
    scope: InterceptorScope,
    hook: InterceptorHook,
    unit: Entity,
    target: Entity,
    interaction: UnitInteraction
) = this[hook]?.filter { it.scope.matches(scope) }?.forEach { it.intercept(unit, target, interaction) }

fun Entity.interceptAsUnit(scope: InterceptorScope, hook: InterceptorHook, target: Entity, interaction: UnitInteraction) =
    interceptors.asUnitInterceptors.intercept(scope, hook, this, target, interaction)
fun Entity.interceptAsTarget(scope: InterceptorScope, hook: InterceptorHook, unit: Entity, interaction: UnitInteraction) =
    interceptors.asTargetInterceptors.intercept(scope, hook, unit, this, interaction)

class TimersComponent : Component {
    var timerSequence = STARTING_SEQUENCE
    val timers = mutableMapOf<Int, Timer>()

    init {
        timers[ATTACK_TIMER] = Timer(0f)
        timers[ABILITY_TIMER] = Timer(0f)
    }

    fun newTimer(targetTime: Float): Int {
        val timerId = ++timerSequence
        timers[timerId] = Timer(targetTime)
        return timerId
    }

    companion object {
        const val ATTACK_TIMER = 0
        const val ABILITY_TIMER = 1
        private const val STARTING_SEQUENCE = 2
    }
}
val timerMapper = mapperFor<TimersComponent>()
val Entity.timerInfo get() = this[timerMapper]!!
val Entity.timers get() = timerInfo.timers
val Entity.attackTimer get() = timers[TimersComponent.ATTACK_TIMER]!!
val Entity.abilityTimer get() = timers[TimersComponent.ABILITY_TIMER]!!

class TargetComponent(var target: Int) : Component
val targetMapper = mapperFor<TargetComponent>()
val Entity.target get() = this[targetMapper]?.target
val Entity.hasTarget get() = target != null

class ActionsComponent : Component {
    val queue = Channel<UnitAction>()
    val blockers = mutableListOf<ActionBlocker>()
}
val actionsMapper = mapperFor<ActionsComponent>()
val Entity.actions get() = this[actionsMapper]!!

class BattleDataComponent(maxHp: Float) : Component {
    var currentHp = maxHp
}
val battleDataMapper = mapperFor<BattleDataComponent>()
val Entity.battleData get() = this[battleDataMapper]!!
var Entity.currentHp
    get() = battleData.currentHp
    set(value) { battleData.currentHp = value }

class AliveComponent : Component
val aliveMapper = mapperFor<AliveComponent>()
val Entity.alive get() = this[aliveMapper]
val Entity.isAlive get() = alive != null
val aliveUnitFamily = allOf(UnitComponent::class, AliveComponent::class).get()!!

class EffectsComponent : Component {
    val effects = mutableListOf<Effect>()
}
val effectsMapper = mapperFor<EffectsComponent>()
val Entity.effects get() = this[effectsMapper]!!.effects
