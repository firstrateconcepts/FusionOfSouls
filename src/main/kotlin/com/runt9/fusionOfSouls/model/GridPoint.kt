package com.runt9.fusionOfSouls.model

import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.userGridWidth
import com.soywiz.korma.geom.IPoint
import kotlin.math.abs

operator fun GridPoint.minus(that: GridPoint) = GridPoint(x - that.x, y - that.y)

class GridPoint(
    override val x: Double,
    override val y: Double,
    var isBlocked: Boolean = false,
    val worldX: Double = x * cellSize,
    val worldY: Double = y * cellSize,
    val userGridCellIndex: Int = (flipY(y) * userGridWidth + x).toInt()
): IPoint {
    companion object {
        fun fromUserGridCellIndex(index: Int): GridPoint {
            val x = index % userGridWidth
            val y = index / userGridWidth
            return GridPoint(x.toDouble(), flipY(y).toDouble())
        }

        fun flipY(y: Number) = gridHeight - 1 - y.toInt()

        operator fun invoke(x: Int, y: Int) = GridPoint(x.toDouble(), y.toDouble())
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
        return "($x, $y)"
    }
}
