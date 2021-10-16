package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import kotlinx.coroutines.channels.Channel
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.action.ActionBlocker
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.model.unit.effect.Effect
import net.firstrateconcepts.fusionofsouls.util.ext.Timer

class UnitComponent(val type: UnitType, val team: UnitTeam, val ability: AbilityDefinition) : Component
val unitFamily = oneOf(UnitComponent::class).get()!!
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!
val Entity.team get() = unitInfo.team
val Entity.ability get() = unitInfo.ability

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
