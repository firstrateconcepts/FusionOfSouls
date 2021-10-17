package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import kotlin.random.Random

class RandomizerService(private val runStateService: RunStateService, eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    lateinit var rng: Random

    override fun startInternal() {
        val state = runStateService.load()
        rng = Random(state.seed.hashCode())
    }

    fun <T> randomize(action: (Random) -> T) = action(rng)

    fun percentChance(percentChance: Float) = randomize { it.nextFloat() <= percentChance }
    fun attackRoll() = randomize { it.nextInt(0, 100) }
}
