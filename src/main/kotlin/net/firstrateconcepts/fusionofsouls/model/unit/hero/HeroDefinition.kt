package net.firstrateconcepts.fusionofsouls.model.unit.hero

import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture

sealed interface HeroDefinition {
    val id: Int
    val name: String
    val texture: UnitTexture
    val unitCap: Int
    val runeCap: Int
    val fusionCap: Int
}

fun getHeroDefinitionForId(id: Int) = HeroDefinition::class.sealedSubclasses.mapNotNull { it.objectInstance }.find { it.id == id }
