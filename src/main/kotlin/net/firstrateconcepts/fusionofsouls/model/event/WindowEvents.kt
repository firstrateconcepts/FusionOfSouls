package net.firstrateconcepts.fusionofsouls.model.event

import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.FosScreen
import kotlin.reflect.KClass

class ChangeScreenRequest<S : FosScreen>(val screenClass: KClass<S>) : Event
inline fun <reified S : FosScreen> changeScreenRequest() = ChangeScreenRequest(S::class)
inline fun <reified S : FosScreen> EventBus.enqueueChangeScreen() = enqueueEventSync(changeScreenRequest<S>())

class ShowDialogRequest<D : DialogController>(val dialogClass: KClass<D>) : Event
inline fun <reified D : DialogController> showDialogRequest() = ShowDialogRequest(D::class)
inline fun <reified S : DialogController> EventBus.enqueueShowDialog() = enqueueEventSync(showDialogRequest<S>())

class ExitRequest : Event
fun EventBus.enqueueExitRequest() = enqueueEventSync(ExitRequest())
