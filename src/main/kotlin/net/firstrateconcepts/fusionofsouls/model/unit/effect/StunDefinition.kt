package net.firstrateconcepts.fusionofsouls.model.unit.effect

import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction

object StunDefinition : EffectDefinition {
    override val name = "Stun"
    override val description = "Stunned units take no action for the duration of the effect"
    override val strategies = listOf(ActionBlockerEffectStrategy(UnitAction::class))
}
