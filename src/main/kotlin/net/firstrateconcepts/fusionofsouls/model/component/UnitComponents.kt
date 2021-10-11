package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import kotlinx.coroutines.channels.Channel
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.action.ActionBlocker
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.util.ext.Timer

class UnitComponent(val type: UnitType, val team: UnitTeam) : Component
val unitFamily = oneOf(UnitComponent::class).get()!!
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!
val Entity.team get() = unitInfo.team

class TimersComponent : Component {
    val timers = mutableMapOf<Int, Timer>()

    init {
        timers[ATTACK_TIMER] = Timer(0f)
        timers[ABILITY_TIMER] = Timer(0f)
    }

    companion object {
        const val ATTACK_TIMER = 0
        const val ABILITY_TIMER = 1
    }
}
val timerMapper = mapperFor<TimersComponent>()
val Entity.timers get() = this[timerMapper]!!.timers
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
