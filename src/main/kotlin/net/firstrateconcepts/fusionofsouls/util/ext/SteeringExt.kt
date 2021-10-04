package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

fun Vector2.toAngle() = atan2(-x.toDouble(), y.toDouble()).toFloat()

fun Float.toVector(outVector: Vector2): Vector2 {
    outVector.x = (-sin(toDouble())).toFloat()
    outVector.y = cos(toDouble()).toFloat()
    return outVector
}
