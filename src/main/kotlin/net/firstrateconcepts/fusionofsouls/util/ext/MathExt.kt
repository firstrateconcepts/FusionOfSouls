package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.math.MathUtils
import kotlin.math.sqrt
import kotlin.random.Random

const val PERCENT_MULTI = 100

fun Float.percent() = this * PERCENT_MULTI
fun Double.percent() = this * PERCENT_MULTI
val Float.radDeg get() = this * MathUtils.radDeg
val Float.degRad get() = this * MathUtils.degRad
fun Double.sqrt() = sqrt(this)
fun Float.sqrt() = sqrt(this)
fun Int.sqrt() = toDouble().sqrt()

fun ClosedRange<Float>.random(rng: Random) = rng.nextFloat() * (endInclusive - start) + start

fun Float.displayRound(decimals: Int = 2) = "%.${decimals}f".format(this)
