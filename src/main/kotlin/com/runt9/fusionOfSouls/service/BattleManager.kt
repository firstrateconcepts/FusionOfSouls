package com.runt9.fusionOfSouls.service

import com.badlogic.gdx.scenes.scene2d.Group
import com.kotcrab.vis.ui.widget.Draggable
import com.runt9.fusionOfSouls.model.event.EndTurnEvent
import com.runt9.fusionOfSouls.model.event.StartTurnEvent
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.kmem.toIntFloor
import com.soywiz.korev.dispatch
import kotlinx.coroutines.launch
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.scene2d.KStack
import ktx.scene2d.vis.KVisTable
import kotlin.math.max

enum class BattleStatus {
    BEFORE, DURING, AFTER
}

data class BattleContext(val enemyCount: Int, var flawless: Boolean = true, var heroLived: Boolean = true)

// TODO: Detach view logic (container + unit drawing) from service logic
class BattleManager(
    private val gridService: GridService,
    private val pathService: PathService,
    private val unitManager: BattleUnitManager,
    private val enemyGenerator: EnemyGenerator
) {
    private var battleStatus = BattleStatus.BEFORE
    lateinit var onBattleComplete: (Team) -> Unit

    fun newBattle(gridContainer: Group, playerUnitGrid: KVisTable) {
        runState.activeUnits.filter { it.savedGridPos != null }.forEach {
            val playerUnit = BattleUnit(it, Team.PLAYER)
//            playerUnitGrid += playerUnit
            playerUnit.setInitialPosition()
            unitManager.playerTeam += playerUnit
        }
        gridService.blockAll(unitManager.playerTeam.map(BattleUnit::gridPos))

        val hero = BattleUnit(runState.hero, Team.PLAYER)
        val drag = Draggable()
        drag.attachTo(hero)
        playerUnitGrid.cells[24].actor as KStack += hero
//        hero.setInitialPosition()
        unitManager.playerTeam += hero
        gridService.block(hero.gridPos)
//        playerUnitGrid.initialized = true

        // TODO: Algorithm for floor/room changes # and strength of enemies

        val enemyCount = max((((runState.floor - 1) * 10.0) + runState.room) / 3.0, 1.0).toIntFloor()
        enemyGenerator.generateEnemies(enemyCount, -25.0).forEach {
            gridContainer += (it)
            it.setInitialPosition()
            unitManager.enemyTeam += it
        }
        runState.battleContext = BattleContext(enemyCount)
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

        unit.dispatch(EndTurnEvent(unit))
    }

    private fun battleComplete(team: Team) {
        runState.activeUnits.forEach(GameUnit::reset)
        runState.hero.reset()
        unitManager.clear()
        battleStatus = BattleStatus.AFTER
        onBattleComplete(team)
        gridService.reset()
    }
}
