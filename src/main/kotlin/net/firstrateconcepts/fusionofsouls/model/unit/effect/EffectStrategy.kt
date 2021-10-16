package net.firstrateconcepts.fusionofsouls.model.unit.effect

import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import kotlin.reflect.KClass

interface EffectStrategy
class ActionBlockerEffectStrategy(vararg val actions: KClass<out UnitAction>) : EffectStrategy
