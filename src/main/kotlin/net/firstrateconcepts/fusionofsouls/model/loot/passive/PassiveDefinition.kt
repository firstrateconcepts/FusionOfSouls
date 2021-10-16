package net.firstrateconcepts.fusionofsouls.model.loot.passive

interface PassiveDefinition {
    val id: Int
    val name: String
    val description: String
    val strategy: PassiveStrategy
}
