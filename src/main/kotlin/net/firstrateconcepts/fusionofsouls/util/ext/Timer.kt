package net.firstrateconcepts.fusionofsouls.util.ext

import kotlin.math.min

class Timer(var targetTime: Float) {
    private var elapsedTime = 0f
    val percentComplete get() = elapsedTime / targetTime
    val isReady get() = elapsedTime >= targetTime && !isPaused
    var isPaused = false

    fun tick(time: Float) {
        if (isReady || isPaused) return
        elapsedTime += time
    }

    fun reset(rollover: Boolean = true) {
        elapsedTime = if (rollover) min(0f, elapsedTime - targetTime) else 0f
    }

    fun pause() { isPaused = true }
    fun resume() { isPaused = false }
}
