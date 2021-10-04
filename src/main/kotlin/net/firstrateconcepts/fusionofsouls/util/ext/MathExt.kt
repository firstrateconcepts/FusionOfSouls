package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.math.MathUtils

const val PERCENT_MULTI = 100

fun Float.percent() = this * PERCENT_MULTI
fun Double.percent() = this * PERCENT_MULTI
val Float.radDeg get() = this * MathUtils.radDeg
val Float.degRad get() = this * MathUtils.degRad
