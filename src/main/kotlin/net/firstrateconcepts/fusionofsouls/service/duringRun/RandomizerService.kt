package net.firstrateconcepts.fusionofsouls.service.duringRun

import kotlin.random.Random

class RandomizerService(private val runStateService: RunStateService) : RunService() {
    lateinit var rng: Random

    override fun startInternal() {
        val state = runStateService.load()
        rng = Random(state.seed.hashCode())
    }

    fun <T> randomize(action: (Random) -> T) = action(rng)
}
