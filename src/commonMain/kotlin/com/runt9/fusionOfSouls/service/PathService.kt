package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.kds.intArrayListOf
import kotlin.math.abs

// TODO: This probably needs some refactoring and some code cleanup, mostly copy/pasted from proof-of-concept
class PathService(private val gridService: GridService, private val unitManager: BattleUnitManager) {
    fun findNextPoint(unit: BattleUnit): GridPoint {
        var nextPoint = if (unit.target != null) {
            nextTowardsTarget(unit)
        } else {
            nextForward(unit)
        }

        if (nextPoint == null) {
            println("[${unit.name}]: Path to target blocked, resetting")
            unit.removeTarget()
            nextPoint = nextForward(unit)
        }

        return nextPoint
    }

    private fun nextForward(unit: BattleUnit): GridPoint {
        val pos = unit.gridPos
        val rx = ((gridWidth - 1) / 2.0).compareTo(pos.x)
        val ry = findDirectionOfNextColumnOpen(unit)

        intArrayListOf(rx, 0, -rx).forEach { nextX ->
            intArrayListOf(0, ry, -ry).forEach { nextY ->
                gridService.tryGet((pos.x + nextX).toInt(), (pos.y + nextY).toInt())?.let { testPoint ->
                    if (!testPoint.isBlocked && testPoint != pos && testPoint != unit.previousGridPos && !crossesExistingMovement(pos, testPoint)) {
                        return testPoint
                    }
                }
            }
        }

        return pos
    }

    private fun findDirectionOfNextColumnOpen(unit: BattleUnit): Int {
        val rx = ((gridWidth - 1) / 2.0).compareTo(unit.gridPos.x)

        var nextClosestYPoint = gridService.findBy { it.x == unit.gridPos.x + rx && !it.isBlocked }.minByOrNull { abs(it.y - (gridHeight / 2)) }
        if (nextClosestYPoint == null) {
            nextClosestYPoint = gridService.findBy { it.x == unit.gridPos.x && !it.isBlocked }.minByOrNull { abs(it.y - (gridHeight / 2)) }
        }
        return nextClosestYPoint?.y?.compareTo(unit.gridPos.y) ?: 0
    }

    private fun crossesExistingMovement(start: GridPoint, end: GridPoint) = unitManager.allUnits
        .filter { it.movingToGridPos != null && it.gridPos != start && it.gridPos != end }
        .any { unit ->
            val unitPos = unit.gridPos
            val unitMove = unit.movingToGridPos!!

            abs(unitPos.x - unitMove.x) == 1.0 &&
                    abs(unitPos.y - unitMove.y) == 1.0 &&
                    abs(start.x - end.x) == 1.0 &&
                    abs(start.y - end.y) == 1.0 &&
                    unitPos.isAdjacentTo(start) &&
                    unit.gridPos.isAdjacentTo(end) &&
                    unitMove.isAdjacentTo(start) &&
                    unitMove.isAdjacentTo(end)
        }

    private fun nextTowardsTarget(unit: BattleUnit): GridPoint? {
        val attackRange = unit.unit.attackRange
        val pos = unit.gridPos
        val rx = ((gridWidth - 1) / 2.0).compareTo(pos.x)
        val ry = findDirectionOfNextColumnOpen(unit)
        val safeTarget = unit.target!!
        val targetPos = if (safeTarget.movingToGridPos != null) safeTarget.movingToGridPos!! else safeTarget.gridPos

        if (pos.isWithinRange(targetPos, attackRange)) {
            return pos
        }

        val baseDistance = pos.manhattanDistance(targetPos)
        var shortestDistance = Double.MAX_VALUE
        var possibleFirstNode: GridPoint? = null
        val yList = if (ry == 0) intArrayListOf(0, 1, -1) else intArrayListOf(0, ry, -ry)
        intArrayListOf(rx, 0, -rx).forEach { nextX ->
            yList.forEach { nextY ->
                gridService.tryGet((pos.x + nextX).toInt(), (pos.y + nextY).toInt())?.let { testPoint ->
                    if (testPoint.isBlocked || testPoint == unit.previousGridPos || testPoint == pos || crossesExistingMovement(pos, testPoint)) {
                        return@let
                    }

                    if (testPoint.isWithinRange(targetPos, attackRange)) {
                        return testPoint
                    }

                    // TODO: Maybe not the right spot for this
                    // If the tile is adjacent to a non-target enemy, change target to that enemy and return the point
                    val possibleNewTargets = getPossibleNewTargets(unitManager.enemyTeamOf(unit), testPoint, attackRange)
                    if (possibleNewTargets.isNotEmpty()) {
                        unit.changeTarget(possibleNewTargets.first())
                        return testPoint
                    }

                    val distance = testPoint.manhattanDistance(targetPos)
                    if (distance < baseDistance || (distance < shortestDistance && checkNextStep(
                            testPoint,
                            distance,
                            listOf(pos),
                            targetPos,
                            attackRange,
                            unitManager.enemyTeamOf(unit)
                        ))
                    ) {
                        shortestDistance = distance
                        possibleFirstNode = testPoint

                    }

                    // TODO: Can likely optimize by stopping after hitting like 2 longer distances in a row or something
                }
            }
        }

        return possibleFirstNode
    }

    private fun checkNextStep(
        startingPoint: GridPoint,
        bestDistanceSoFar: Double,
        previousPoints: Collection<GridPoint>,
        targetPos: GridPoint,
        attackRange: Int,
        enemies: Collection<BattleUnit>
    ): Boolean {
        val rx = ((gridWidth - 1) / 2.0).compareTo(startingPoint.x)
        val ry = ((gridWidth - 1) / 2.0).compareTo(startingPoint.y)

        intArrayListOf(rx, 0, -rx).forEach { nextX ->
            intArrayListOf(0, ry, -ry).forEach { nextY ->
                gridService.tryGet((startingPoint.x + nextX).toInt(), (startingPoint.y + nextY).toInt())?.let { testPoint ->
                    if (testPoint.isBlocked || previousPoints.any { it == testPoint } || testPoint == startingPoint) {
                        return@let
                    }

                    if (testPoint.isWithinRange(targetPos, attackRange)) {
                        return true
                    }

                    val possibleNewTargets = getPossibleNewTargets(enemies, testPoint, attackRange)
                    if (possibleNewTargets.isNotEmpty()) {
                        return true
                    }

                    val distance = testPoint.manhattanDistance(targetPos)
                    if (distance < bestDistanceSoFar && checkNextStep(testPoint, distance, previousPoints + testPoint, targetPos, attackRange, enemies)) {
                        return true
                    }

                    // TODO: Can likely optimize by stopping after hitting like 2 longer distances in a row or something
                }
            }
        }

        return false
    }

    private fun getPossibleNewTargets(enemies: Collection<BattleUnit>, testPoint: GridPoint, attackRange: Int) =
        enemies.filter { testPoint.isWithinRange(it.gridPos, attackRange) || (it.movingToGridPos != null && testPoint.isWithinRange(it.movingToGridPos!!, attackRange)) }
}
