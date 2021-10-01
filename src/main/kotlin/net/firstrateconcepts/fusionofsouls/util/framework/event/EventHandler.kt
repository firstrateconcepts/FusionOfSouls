package net.firstrateconcepts.fusionofsouls.util.framework.event

interface EventHandler<in E : Event> {
    suspend fun handle(event: E)
}

inline fun <reified E : Event> eventHandler(crossinline handler: (E) -> Unit) = object : EventHandler<E> {
    override suspend fun handle(event: E) = handler(event)
}
