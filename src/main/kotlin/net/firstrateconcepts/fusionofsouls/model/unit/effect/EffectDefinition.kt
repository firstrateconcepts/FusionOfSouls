package net.firstrateconcepts.fusionofsouls.model.unit.effect

interface EffectDefinition {
    val name: String
    val description: String
    val strategies: List<EffectStrategy>
}
