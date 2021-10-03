package net.firstrateconcepts.fusionofsouls.service.duringRun

import kotlin.random.Random

class RandomizerService(registry: RunServiceRegistry, private val runStateService: RunStateService) : RunService {
    lateinit var rng: Random

    init {
        registry.register(this)
    }

    override fun start() {
        val state = runStateService.load()
        rng = Random(state.seed.hashCode())
    }
}
