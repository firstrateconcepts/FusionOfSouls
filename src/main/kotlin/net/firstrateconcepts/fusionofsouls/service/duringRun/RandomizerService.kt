package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import kotlin.random.Random

class RandomizerService(private val runStateService: RunStateService, override val eventBus: EventBus) : RunService() {
    lateinit var rng: Random

    override fun startInternal() {
        val state = runStateService.load()
        rng = Random(state.seed.hashCode())
    }

    fun <T> randomize(action: (Random) -> T) = action(rng)
}
