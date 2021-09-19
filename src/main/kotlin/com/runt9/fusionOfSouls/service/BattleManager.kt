package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.event.EndTurnEvent
import com.runt9.fusionOfSouls.model.event.StartTurnEvent
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.kmem.toIntFloor
import com.soywiz.korev.dispatch
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import kotlin.math.max

class BattleManager(
    private val gridService: GridService,
    private val pathService: PathService,
    private val unitManager: BattleUnitManager,
    private val enemyGenerator: EnemyGenerator
) {
    lateinit var onBattleComplete: (Team) -> Unit

    fun newBattle() {
        runState.battleStatus = BattleStatus.BEFORE
        runState.activeUnits.forEach { unitManager.playerTeam += it }
        gridService.blockAll(unitManager.playerTeam.map(BattleUnit::gridPos))

        val hero = BattleUnit(runState.hero, Team.PLAYER)
        unitManager.playerTeam += hero
        gridService.block(hero.gridPos)

        // TODO: Algorithm for floor/room changes # and strength of enemies

        val enemyCount = max((((runState.floor - 1) * 10.0) + runState.room) / 3.0, 1.0).toIntFloor()
        val enemies = enemyGenerator.generateEnemies(enemyCount, -25.0)
        enemies.forEach { unitManager.enemyTeam += it }
        runState.battleContext = BattleContext(enemies, hero)
    }

    fun unitAddedToBattle(unit: BattleUnit) {
        unitManager.playerTeam += unit
        gridService.block(unit.gridPos)
    }

    fun unitRemovedFromBattle(unit: BattleUnit) {
        unitManager.playerTeam -= unit
        gridService.unblock(unit.gridPos)
    }

    fun start() {
        runState.battleStatus = BattleStatus.DURING
        unitManager.initSkills()
    }

    suspend fun handleTurn() {
        if (runState.battleStatus != BattleStatus.DURING) {
            return
        }

        if (unitManager.playerTeam.isEmpty()) {
            battleComplete(Team.ENEMY)
        } else if (unitManager.enemyTeam.isEmpty()) {
            battleComplete(Team.PLAYER)
        }

        unitManager.allUnits.filter { it.movingToGridPos != null }.forEach(unitManager::unitMoveComplete)

        unitManager.playerTeam.sortedBy { it.gridPos.x }.reversed().forEach { gu ->
            KtxAsync.launch {
                handleNextTurnForUnit(gu)
            }
        }

        unitManager.enemyTeam.sortedBy { it.gridPos.x }.forEach { gu ->
            KtxAsync.launch {
                handleNextTurnForUnit(gu)
            }
        }
    }

    private suspend fun handleNextTurnForUnit(unit: BattleUnit) {
        unit.dispatch(StartTurnEvent(unit))

        // If in attack range, start attacking
        if (unitManager.checkAttackRange(unit)) {
            unit.dispatch(EndTurnEvent(unit))
            return
        }

        unit.cancelAttacking()
        unit.states.add(BattleUnitState.MOVING)

        unitManager.checkAggroRange(unit)

        // Get next movement square (forward or towards target)
        val nextPoint = pathService.findNextPoint(unit)

        // Initiate movement
        if (nextPoint != unit.gridPos) {
            unitManager.handleUnitMovement(unit, nextPoint)
        }

        unit.dispatch(EndTurnEvent(unit))
    }

    private fun battleComplete(team: Team) {
        runState.activeUnits.forEach { it.unit.reset() }
        runState.hero.reset()
        unitManager.clear()
        runState.battleStatus = BattleStatus.AFTER
        onBattleComplete(team)
        gridService.reset()
    }
}
