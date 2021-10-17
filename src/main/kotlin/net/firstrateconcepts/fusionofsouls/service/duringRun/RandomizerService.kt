package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.loot.Rarity
import net.firstrateconcepts.fusionofsouls.util.ext.random
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import kotlin.math.roundToInt
import kotlin.random.Random

class RandomizerService(private val runStateService: RunStateService, eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    lateinit var rng: Random

    override fun startInternal() {
        val state = runStateService.load()
        rng = Random(state.seed.hashCode())
    }

    fun <T> randomize(action: (Random) -> T) = action(rng)

    fun percentChance(percentChance: Float) = rng.nextFloat() <= percentChance
    fun attackRoll() = rng.nextInt(0, 100)
    fun coinFlip() = rng.nextBoolean()

    fun randomAttributeModifier(type: AttributeType, rarity: Rarity): AttributeModifier {
        val multiplier = rarity.ordinal + 1f
        val range = type.definition.rangeForRandomizer

        return if (coinFlip())
            AttributeModifier(type, flatModifier = range.flat.random(rng) * multiplier)
        else
            AttributeModifier(type, percentModifier = range.percent.random(rng).roundToInt() * multiplier)
    }

}
