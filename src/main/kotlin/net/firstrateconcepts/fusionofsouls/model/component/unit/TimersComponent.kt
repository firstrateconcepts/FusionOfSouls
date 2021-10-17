package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.util.ext.Timer

var timerSequence = 0

class TimersComponent : Component {
    val timers = mutableMapOf<Int, Timer>()

    fun newTimer(targetTime: Float): Int {
        val timerId = ++timerSequence
        timers[timerId] = Timer(targetTime)
        return timerId
    }
}

val timerMapper = mapperFor<TimersComponent>()
val Entity.timerInfo get() = this[timerMapper]!!
val Entity.timers get() = timerInfo.timers
