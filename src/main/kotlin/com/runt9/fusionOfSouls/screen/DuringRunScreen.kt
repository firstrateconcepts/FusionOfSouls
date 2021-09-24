package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Timer
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.UnitGenerator
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.fosVisTable
import com.runt9.fusionOfSouls.view.duringRun.InGameMenuDialog
import com.runt9.fusionOfSouls.view.duringRun.PostBattleDialog
import com.runt9.fusionOfSouls.view.duringRun.TopBar
import com.runt9.fusionOfSouls.view.duringRun.battleArea.BattleArea
import com.runt9.fusionOfSouls.view.duringRun.unitBench.UnitBench
import com.soywiz.korio.async.launch
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.async.KtxAsync
import ktx.async.interval
import ktx.scene2d.actor
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton


// TODO: During post battle rewards, disable dragging but allow seeing units. This is important to decide on a unit choice for class synergies as well as potential fusions opportunities
class DuringRunScreen(
    private val game: FosGame,
    private val battleManager: BattleManager,
    private val unitGenerator: UnitGenerator,
    private val battleArea: BattleArea,
    private val unitBench: UnitBench,
    private val topBar: TopBar,
    private val inGameMenuDialog: InGameMenuDialog,
    override val stage: Stage
) : FosScreen {
    private lateinit var updater: Timer.Task

    override fun show() {
        battleManager.onBattleComplete = { team -> onBattleComplete(team) }
        stage.actors {
            actor(battleArea)
            actor(topBar)
            actor(unitBench)
        }

        newBattle()
    }

    override fun hide() {
        stage.clear()
    }

    private fun drawStartButton() {
        stage.actors {
            visTextButton("Start Battle") {
                centerPosition()
                toFront()

                onClick {
                    remove()
                    KtxAsync.launch { startBattle() }
                }
            }
        }
    }

    private fun onBattleComplete(team: Team) {
        // Show post-battle rewards
        // After post-battle rewards, start new battle
        println("$team won!")
        updater.cancel()

        if (team == Team.ENEMY) {
            drawLost()
            return
        }

        drawPostBattle()

        if (runState.room == 10) {
            runState.floor++
            runState.room = 1
        } else {
            runState.room++
        }

        println("Now on Floor ${runState.floor} Room ${runState.room}")
    }

    private fun drawLost() {
        stage.actors {
            fosVisTable {
                visLabel("You Lost!")
                row()
                visTextButton("Return to Menu") {
                    onClick {
                        game.reset()
                        game.setScreen<MainMenuScreen>()
                    }
                }
            }
        }
    }

    private fun drawPostBattle() {
        val dialog = PostBattleDialog(unitGenerator) { newBattle() }
        dialog.show(this@DuringRunScreen.stage)
    }

    private fun newBattle() {
        drawStartButton()
        battleManager.newBattle()
        battleArea.drawUnits()
    }

    private suspend fun startBattle() {
        battleManager.start()
        updater = interval(1f) {
            KtxAsync.launch { battleManager.handleTurn() }
        }
    }
}
