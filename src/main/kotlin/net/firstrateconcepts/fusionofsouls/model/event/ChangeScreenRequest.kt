package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.util.ext.FosScreen
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import kotlin.reflect.KClass

class ChangeScreenRequest<S : FosScreen>(val screenClass: KClass<S>) : Event

inline fun <reified S : FosScreen> changeScreenRequest() = ChangeScreenRequest(S::class)
