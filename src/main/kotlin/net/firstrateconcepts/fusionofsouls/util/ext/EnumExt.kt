package net.firstrateconcepts.fusionofsouls.util.ext

inline fun <reified E : Enum<E>> Int.matchOrdinal() = enumValues<E>().find { it.ordinal == this }
