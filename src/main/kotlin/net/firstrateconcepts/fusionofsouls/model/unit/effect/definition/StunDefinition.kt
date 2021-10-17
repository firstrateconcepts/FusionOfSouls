package net.firstrateconcepts.fusionofsouls.model.unit.effect.definition

import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitActionType
import net.firstrateconcepts.fusionofsouls.model.unit.effect.ActionBlockerEffectStrategy

object StunDefinition : EffectDefinition {
    override val name = "Stun"
    override val description = "Stunned units take no action for the duration of the effect"
    override val strategies = listOf(ActionBlockerEffectStrategy(*UnitActionType.values()))
}
