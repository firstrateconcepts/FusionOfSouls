package com.runt9.fusionOfSouls.extension

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.view.BattleUnit
import kotlin.math.abs

fun BattleUnit.isAdjacentTo(others: Collection<BattleUnit>): Boolean {
    return others.any { it.isAdjacentTo(this) }
}

fun BattleUnit.isAdjacentTo(other: BattleUnit): Boolean {
    val selfPos = gridPos
    val otherPos = other.gridPos
    return selfPos.isAdjacentTo(otherPos)
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
