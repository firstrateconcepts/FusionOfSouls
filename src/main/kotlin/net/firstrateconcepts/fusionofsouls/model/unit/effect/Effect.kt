package net.firstrateconcepts.fusionofsouls.model.unit.effect

import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitActionType
import net.firstrateconcepts.fusionofsouls.model.unit.effect.definition.EffectDefinition

data class Effect(val definition: EffectDefinition, val timerId: Int)
interface EffectStrategy
class ActionBlockerEffectStrategy(vararg val actions: UnitActionType) : EffectStrategy
