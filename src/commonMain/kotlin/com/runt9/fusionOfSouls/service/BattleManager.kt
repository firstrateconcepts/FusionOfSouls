package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.korge.view.Container
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.coroutines.CoroutineContext

enum class BattleStatus {
    BEFORE, DURING, AFTER
}

class BattleManager(
    private val runState: RunState,
    private val gridService: GridService,
    private val pathService: PathService,
    private val unitManager: BattleUnitManager,
    private val coroutineContext: CoroutineContext,
    val onBattleComplete: suspend (Team) -> Unit
) : Container() {
    private var battleStatus = BattleStatus.BEFORE

    suspend fun newBattle() {
        runState.units.filter { it.savedGridPos != null }.forEach { unitManager.playerTeam.add(BattleUnit(it, Team.PLAYER)) }
        gridService.blockAll(unitManager.playerTeam.map(BattleUnit::gridPos))

        val hero = BattleUnit(runState.hero, Team.PLAYER)
        unitManager.playerTeam.add(hero)
        gridService.block(hero.gridPos)

        // TODO: Algorithm for floor/room changes # of enemies
        val randomEnemyPoint = gridService.addRandomlyToGrid(gridWidth - 5, gridWidth - 1)
        val enemyUnit = GameUnit("enemy", resourcesVfs["redArrow-tp.png"].readBitmap())
        enemyUnit.savedGridPos = randomEnemyPoint
        unitManager.enemyTeam.add(BattleUnit(enemyUnit, Team.ENEMY))
    }

    fun start() {
        battleStatus = BattleStatus.DURING
    }

    suspend fun handleTurn() {
        if (battleStatus != BattleStatus.DURING) {
            return
        }

        if (unitManager.playerTeam.isEmpty()) {
            battleComplete(Team.ENEMY)
        } else if (unitManager.enemyTeam.isEmpty()) {
            battleComplete(Team.PLAYER)
        }

        unitManager.allUnits.filter { it.movingToGridPos != null }.forEach(unitManager::unitMoveComplete)

        unitManager.playerTeam.sortedBy { it.gridPos.x }.reversed().forEach { gu ->
            launchImmediately(coroutineContext) {
                handleNextTurnForUnit(gu)
            }
        }

        unitManager.enemyTeam.sortedBy { it.gridPos.x }.forEach { gu ->
            launchImmediately(coroutineContext) {
                handleNextTurnForUnit(gu)
            }
        }
    }

    private suspend fun handleNextTurnForUnit(unit: BattleUnit) {
        // If in attack range, start attacking
        if (unitManager.checkAttackRange(unit)) {
            return
        }

        unitManager.cancelAttacking(unit)
        unit.states.add(BattleUnitState.MOVING)

        unitManager.checkAggroRange(unit)

        // Get next movement square (forward or towards target)
        val nextPoint = pathService.findNextPoint(unit)

        // Initiate movement
        if (nextPoint != unit.gridPos) {
            unitManager.handleUnitMovement(unit, nextPoint)
        }
    }

    private suspend fun battleComplete(team: Team) {
        runState.units.forEach(GameUnit::purgeTemporaryModifiers)
        unitManager.clear()
        battleStatus = BattleStatus.AFTER
        onBattleComplete(team)
        gridService.reset()
    }
}
