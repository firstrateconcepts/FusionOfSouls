package net.firstrateconcepts.fusionofsouls.model.unit.effect.definition

import net.firstrateconcepts.fusionofsouls.model.unit.effect.EffectStrategy

interface EffectDefinition {
    val name: String
    val description: String
    val strategies: List<EffectStrategy>
}
