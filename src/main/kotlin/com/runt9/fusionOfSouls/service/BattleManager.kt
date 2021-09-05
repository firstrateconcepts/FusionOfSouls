package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.event.EndTurnEvent
import com.runt9.fusionOfSouls.model.event.StartTurnEvent
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.korev.dispatch
import kotlinx.coroutines.launch
import ktx.async.KtxAsync

enum class BattleStatus {
    BEFORE, DURING, AFTER
}

// TODO: Detach view logic (container + unit drawing) from service logic
class BattleManager(
    private val runState: RunState,
    private val gridService: GridService,
    private val pathService: PathService,
    private val unitManager: BattleUnitManager,
    private val enemyGenerator: EnemyGenerator
) {
    private var battleStatus = BattleStatus.BEFORE
    lateinit var onBattleComplete: suspend (Team) -> Unit

    suspend fun newBattle() {
        runState.units.filter { it.savedGridPos != null }.forEach {
            val playerUnit = BattleUnit(it, Team.PLAYER)
//            playerUnit.draw(gridContainer)
            unitManager.playerTeam.add(playerUnit)
        }
        gridService.blockAll(unitManager.playerTeam.map(BattleUnit::gridPos))

        val hero = BattleUnit(runState.hero, Team.PLAYER)
//        hero.draw(gridContainer)
        unitManager.playerTeam.add(hero)
        gridService.block(hero.gridPos)

        // TODO: Algorithm for floor/room changes # and strength of enemies

        enemyGenerator.generateEnemies(1, -25.0).forEach {
//            it.draw(gridContainer)
            unitManager.enemyTeam.add(it)
        }
    }

    fun start() {
        battleStatus = BattleStatus.DURING
        unitManager.initSkills()
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

//        unit.dispatch(EndTurnEvent(unit))
    }

    private suspend fun battleComplete(team: Team) {
        runState.units.forEach(GameUnit::reset)
        runState.hero.reset()
        unitManager.clear()
        battleStatus = BattleStatus.AFTER
        onBattleComplete(team)
        gridService.reset()
    }
}
