package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.kds.Array2
import com.soywiz.korma.math.betweenInclusive
import kotlin.math.abs
import kotlin.math.roundToInt

class GridService {
    private val grid = Array2.withGen(gridWidth, gridHeight) { x, y -> GridPoint(x.toDouble(), y.toDouble()) }

    fun get(x: Int, y: Int): GridPoint {
        return grid[x, y]
    }

    fun get(gridPoint: GridPoint): GridPoint {
        return get(gridPoint.x.roundToInt(), gridPoint.y.roundToInt())
    }

    fun reset() {
        grid.forEach { it.isBlocked = false }
    }

    fun blockAll(points: List<GridPoint>) {
        points.forEach(this::block)
    }

    private fun changeBlocked(gridPos: GridPoint, blocked: Boolean) {
        grid[gridPos.x.roundToInt(), gridPos.y.roundToInt()].isBlocked = blocked
    }

    fun block(gridPos: GridPoint) {
        changeBlocked(gridPos, true)
    }

    fun unblock(gridPos: GridPoint) {
        changeBlocked(gridPos, false)
    }

    // TODO: Enemies should have a better way of getting added to the grid
    fun addRandomlyToGrid(minX: Int, maxX: Int): GridPoint {
        val randomOpenSpot = grid.filter { it.x.betweenInclusive(minX.toDouble(), maxX.toDouble()) && !it.isBlocked }.random()

        block(randomOpenSpot)
        return randomOpenSpot
    }

    fun unitMovedToCellIndex(unit: BattleUnit, index: Int) {
        val newPoint = GridPoint(index % 4.0, index % gridHeight.toDouble())
        unblock(unit.gridPos)
        block(newPoint)
        unit.gridPos = newPoint
        unit.unit.savedGridPos = newPoint
    }

    fun tryGet(x: Int, y: Int) = grid.tryGet(x, y)
    fun findBy(predicate: (GridPoint) -> Boolean) = grid.filter(predicate)
}

fun GridPoint.isAdjacentTo(other: GridPoint): Boolean {
    return isWithinRange(other, 1)
}

fun BattleUnit.isWithinRange(others: Collection<BattleUnit>, range: Int): Boolean {
    return others.any { it.isWithinRange(this, range) }
}

fun BattleUnit.isWithinRange(other: BattleUnit, range: Int): Boolean {
    val selfPos = gridPos
    val otherPos = other.gridPos
    return selfPos.isWithinRange(otherPos, range)
}

fun GridPoint.isWithinRange(other: GridPoint, range: Int): Boolean {
    return abs(x - other.x) <= range && abs(y - other.y) <= range
}

fun BattleUnit.diagonalTo(other: BattleUnit) = abs(gridPos.x - other.gridPos.x) == 1.0 && abs(gridPos.y - other.gridPos.y) == 1.0
