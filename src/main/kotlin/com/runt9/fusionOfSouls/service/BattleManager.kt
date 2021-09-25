package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.event.EndTurnEvent
import com.runt9.fusionOfSouls.model.event.StartTurnEvent
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.BattleUnitState
import com.soywiz.kmem.toIntCeil
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

    val activeUnitAddedListener = { unit: BasicUnit -> unitAddedToBattle(unit.battleUnit!!) }
    val activeUnitRemovedListener = { unit: BasicUnit -> unitRemovedFromBattle(unit.battleUnit!!) }

    fun newBattle() {
        runState.battleStatus = BattleStatus.BEFORE

        val hero = BattleUnit(runState.hero, Team.PLAYER)
        gridService.block(hero.gridPos)

        // TODO: Please runt write unit tests for these for your own sanity you'll regret it later if you don't

        val enemies = if (runState.room == 11) {
            listOf(BattleUnit(runState.boss, Team.ENEMY))
        } else {
            val enemyCount = max((((runState.floor - 1) * 10.0) + runState.room) / 3.0, 1.0).toIntCeil()
            val enemyStrength = -25.0 + (((((runState.floor - 1) * 10.0) + runState.room) - 1) * 2.5)
            enemyGenerator.generateEnemies(enemyCount, enemyStrength)
        }

        runState.battleContext = BattleContext(enemies, hero).apply {
            runState.activeUnits.forEach { playerTeam += BattleUnit(it, Team.PLAYER) }
            gridService.blockAll(playerTeam.map(BattleUnit::gridPos))
            enemies.forEach { enemyTeam += it }
            playerTeam += hero
        }

        runState.activeUnitAddedListeners += activeUnitAddedListener
        runState.activeUnitRemovedListeners += activeUnitRemovedListener
    }

    fun unitAddedToBattle(unit: BattleUnit) {
        runState.battleContext.playerTeam += unit
        gridService.block(unit.gridPos)
    }

    fun unitRemovedFromBattle(unit: BattleUnit) {
        runState.battleContext.playerTeam -= unit
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

        if (runState.battleContext.playerTeam.isEmpty()) {
            battleComplete(Team.ENEMY)
            return
        } else if (runState.battleContext.enemyTeam.isEmpty()) {
            battleComplete(Team.PLAYER)
            return
        }

        runState.battleContext.allUnits.filter { it.movingToGridPos != null }.forEach(unitManager::unitMoveComplete)

        runState.battleContext.playerTeam.sortedBy { it.gridPos.x }.reversed().forEach { gu ->
            KtxAsync.launch {
                handleNextTurnForUnit(gu)
            }
        }

        runState.battleContext.enemyTeam.sortedBy { it.gridPos.x }.forEach { gu ->
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
        runState.activeUnits.forEach { it.reset() }
        runState.hero.reset()
        runState.battleContext.clear()
        runState.battleStatus = BattleStatus.AFTER
        onBattleComplete(team)
        gridService.reset()
        runState.activeUnitAddedListeners -= activeUnitAddedListener
        runState.activeUnitRemovedListeners -= activeUnitRemovedListener
    }
}
