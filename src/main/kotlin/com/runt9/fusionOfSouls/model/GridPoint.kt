package com.runt9.fusionOfSouls.model

import com.runt9.fusionOfSouls.cellSize
import com.soywiz.korma.geom.IPoint
import kotlin.math.abs

class GridPoint(
    override val x: Double,
    override val y: Double,
    var isBlocked: Boolean = false,
    val worldX: Double = x * cellSize + cellSize / 2,
    val worldY: Double = y * cellSize + cellSize / 2
): IPoint {
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
        return "($x, $y)"
    }
}
