package net.firstrateconcepts.fusionofsouls.model.unit.effect

import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.model.unit.effect.definition.EffectDefinition
import kotlin.reflect.KClass

data class Effect(val definition: EffectDefinition, val timerId: Int)
interface EffectStrategy
class ActionBlockerEffectStrategy(vararg val actions: KClass<out UnitAction>) : EffectStrategy
