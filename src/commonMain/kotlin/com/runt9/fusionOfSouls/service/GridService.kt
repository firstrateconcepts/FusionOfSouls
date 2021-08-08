package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.model.GridPoint
import com.soywiz.kds.Array2
import com.soywiz.korma.math.betweenInclusive

class GridService {
    private val grid = Array2.withGen(gridWidth, gridHeight) { x, y -> GridPoint(x.toDouble(), y.toDouble()) }

    fun get(x: Int, y: Int): GridPoint {
        return grid[x, y]
    }

    fun get(gridPoint: GridPoint): GridPoint {
        return get(gridPoint.x.toInt(), gridPoint.y.toInt())
    }

    fun reset() {
        grid.forEach { it.isBlocked = false }
    }

    fun blockAll(points: List<GridPoint>) {
        points.forEach(this::block)
    }

    private fun changeBlocked(gridPos: GridPoint, blocked: Boolean) {
        grid[gridPos.x.toInt(), gridPos.y.toInt()].isBlocked = blocked
    }

    fun block(gridPos: GridPoint) {
        changeBlocked(gridPos, true)
    }

    fun unblock(gridPos: GridPoint) {
        changeBlocked(gridPos, false)
    }

    // TODO: Enemies should have a better way of getting added to the grid
    fun addRandomlyToGrid(minX: Int, maxX: Int): GridPoint {
        val randomOpenSpot = grid.filter {
            it.x.betweenInclusive(minX.toDouble(), maxX.toDouble()) && !it.isBlocked
        }.random()

        block(randomOpenSpot)
        return randomOpenSpot
    }

    fun tryGet(x: Int, y: Int) = grid.tryGet(x, y)
    fun findBy(predicate: (GridPoint) -> Boolean) = grid.filter(predicate)
}
