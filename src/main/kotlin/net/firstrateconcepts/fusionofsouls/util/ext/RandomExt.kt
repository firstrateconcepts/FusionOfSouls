package net.firstrateconcepts.fusionofsouls.util.ext

import kotlin.random.Random

private val chars = ('A'..'Z') + ('0'..'9')
fun Random.nextAlphaChar() = chars.random(this)
fun Random.randomString(length: Int) = (1..length).map { nextAlphaChar() }.joinToString("")
