package net.firstrateconcepts.fusionofsouls.event

interface Event {
    val name: String
        get() = this::class.simpleName ?: "Event"
}
