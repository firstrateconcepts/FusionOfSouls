package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.attrMods
import net.firstrateconcepts.fusionofsouls.model.component.unit.runes
import net.firstrateconcepts.fusionofsouls.model.loot.Rarity
import net.firstrateconcepts.fusionofsouls.model.loot.Rune
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RuneService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val passiveService: PassiveService,
    private val randomizer: RandomizerService,
    private val attributeService: AttributeService
) : RunService(eventBus, registry) {
    private var runeIdCounter = 0

    fun addRune(hero: Entity, rune: Rune) {
        attributeService.addModifiers(hero, rune.modifiers)
        rune.passive?.let { passiveService.addPassive(hero, it) }
        hero.runes.add(rune)
    }

    fun removeRune(hero: Entity, rune: Rune) {
        attributeService.removeModifiers(hero, rune.modifiers)
        rune.passive?.let { passiveService.removePassive(hero, it) }
        hero.runes.remove(rune)
    }

    fun generateRune(rarity: Rarity): Rune {
        val count = rarity.numRuneAttrs

        val generatedSoFar = mutableListOf<AttributeType>()
        val modifiers = mutableListOf<AttributeModifier>()

        repeat(count) {
            val type = AttributeType.values().filter { !generatedSoFar.contains(it) }.random(randomizer.rng)

            modifiers += randomizer.randomAttributeModifier(type, rarity)
            generatedSoFar += type
        }

        val passive = if (rarity == Rarity.LEGENDARY) passiveService.randomPassive(rarity) else null

        // TODO: Rune name generator
        return Rune(++runeIdCounter, "Rune", modifiers, passive)
    }

    private val Rarity.numRuneAttrs: Int get() = when (this) {
        Rarity.COMMON -> 1
        Rarity.UNCOMMON -> 2
        Rarity.RARE, Rarity.LEGENDARY -> 3
    }
}
