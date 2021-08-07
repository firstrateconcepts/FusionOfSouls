package com.runt9.fusionOfSouls.model

import com.runt9.fusionOfSouls.cellSize
import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.Point
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.math.abs

class GridPoint(
    override val x: Double,
    override val y: Double,
    var isBlocked: Boolean = false,
    val worldX: Double = x * cellSize + cellSize / 2,
    val worldY: Double = y * cellSize + cellSize / 2
) : IPoint {
    companion object {
        fun fromWorldPoint(point: Point) =
            GridPoint(((point.x - cellSize / 2) / cellSize).roundDecimalPlaces(0), ((point.y - cellSize / 2) / cellSize).roundDecimalPlaces(0))
    }

    fun manhattanDistance(other: GridPoint) = abs(x - other.x) + abs(y - other.y)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GridPoint

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "GridPoint(x=$x, y=$y)"
    }
}
