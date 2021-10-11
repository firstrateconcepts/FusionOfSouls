package net.firstrateconcepts.fusionofsouls.model.unit.action

import kotlin.reflect.KClass

class ActionBlocker(val source: Any, val action: KClass<out UnitAction>)
