package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import ktx.ashley.get
import net.firstrateconcepts.fusionofsouls.util.ext.Timer

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
