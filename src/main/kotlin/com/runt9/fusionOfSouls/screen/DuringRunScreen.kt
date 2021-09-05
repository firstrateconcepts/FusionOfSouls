package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Timer
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.fosVisTable
import com.soywiz.korio.async.launch
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.async.KtxAsync
import ktx.async.interval
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton


class DuringRunScreen(private val game: FosGame, private val battleManager: BattleManager, override val stage: Stage) : FosScreen {
    private val benchBarHeight = cellSize
    private val debugMode = false
    private lateinit var updater: Timer.Task

    override fun show() {
        battleManager.onBattleComplete = { team -> onBattleComplete(team) }
        KtxAsync.launch {
            newBattle()
        }
    }

    private fun drawStartButton() {
        stage.actors {
            visTextButton("Start Battle") {
                centerPosition()

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

//        floorRoomDisplay.setText("Room ${runState.floor}:${runState.room}")

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
        stage.actors {
            fosVisTable {
                visLabel("You Won!")
                row()
                visTextButton("Next Battle") {
                    onClick {
                        this@fosVisTable.remove()
                        KtxAsync.launch { newBattle() }
                    }
                }
            }
        }
    }

    private suspend fun newBattle() {
        drawStartButton()
        battleManager.newBattle()
    }

    private suspend fun startBattle() {
        battleManager.start()
        updater = interval(1f) {
            KtxAsync.launch { battleManager.handleTurn() }
        }
    }
}
