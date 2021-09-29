package net.firstrateconcepts.fusionofsouls.util.framework.event

interface Event {
    val name: String get() = this::class.simpleName ?: "Event"
}
