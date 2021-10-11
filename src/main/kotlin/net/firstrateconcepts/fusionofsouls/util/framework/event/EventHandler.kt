package net.firstrateconcepts.fusionofsouls.util.framework.event

import kotlin.reflect.KClass

interface EventHandler<in E : Event> {
    suspend fun handle(event: E)
}

inline fun <reified E : Event> eventHandler(crossinline handler: suspend (E) -> Unit) = object : EventHandler<E> {
    override suspend fun handle(event: E) = handler(event)
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class HandlesEvent(val eventType: KClass<out Event> = Event::class)
