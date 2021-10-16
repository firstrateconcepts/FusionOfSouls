package net.firstrateconcepts.fusionofsouls.model.unit.hero

import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityDefinition

sealed interface HeroDefinition {
    val id: Int
    val name: String
    val texture: UnitTexture
    val ability: AbilityDefinition
    val unitCap: Int
    val runeCap: Int
    val fusionCap: Int
}

fun getHeroDefinitionForId(id: Int) = HeroDefinition::class.sealedSubclasses.mapNotNull { it.objectInstance }.find { it.id == id }
