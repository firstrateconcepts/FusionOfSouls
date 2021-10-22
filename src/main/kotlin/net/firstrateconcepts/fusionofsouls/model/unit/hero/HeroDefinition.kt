package net.firstrateconcepts.fusionofsouls.model.unit.hero

import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.unitClass.UnitClass

sealed interface HeroDefinition {
    val id: Int
    val name: String
    val texture: UnitTexture
    val passive: PassiveDefinition
    val ability: AbilityDefinition
    val classes: List<UnitClass>
    val unitCap: Int
    val runeCap: Int
    val fusionCap: Int
}

fun getHeroDefinitionForId(id: Int) = HeroDefinition::class.sealedSubclasses.mapNotNull { it.objectInstance }.find { it.id == id }
