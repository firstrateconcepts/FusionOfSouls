package net.firstrateconcepts.fusionofsouls.model.loot.passive

import net.firstrateconcepts.fusionofsouls.model.loot.Rarity

interface PassiveDefinition {
    val id: Int
    val name: String
    val description: String
    val rarity: Rarity
    val strategy: PassiveStrategy
}
