package net.firstrateconcepts.fusionofsouls.util.framework.event

interface EventHandler<in T : Event> {
    suspend fun handle(event: T)
}
