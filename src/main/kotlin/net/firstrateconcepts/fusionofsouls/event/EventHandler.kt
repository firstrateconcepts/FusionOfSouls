package net.firstrateconcepts.fusionofsouls.event

interface EventHandler<in T : Event> {
    suspend fun handle(event: T)
}

@Suppress("unused", "UNUSED_PARAMETER")
inline fun <reified T : Event, reified R : Event> EventHandler<T>.canHandleEvent(event: R) = R::class == T::class
