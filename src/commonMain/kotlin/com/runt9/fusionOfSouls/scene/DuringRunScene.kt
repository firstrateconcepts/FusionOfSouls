package com.runt9.fusionOfSouls.scene

import com.runt9.fusionOfSouls.battleHeight
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.bigMargin
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.gridYStart
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alignLeftToLeftOf
import com.soywiz.korge.view.alignLeftToRightOf
import com.soywiz.korge.view.alignRightToRightOf
import com.soywiz.korge.view.alignTopToBottomOf
import com.soywiz.korge.view.alpha
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.container
import com.soywiz.korge.view.fixedSizeContainer
import com.soywiz.korge.view.graphics
import com.soywiz.korge.view.roundRect
import com.soywiz.korge.view.setText
import com.soywiz.korge.view.solidRect
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.lang.Cancellable
import com.soywiz.korio.lang.cancel


class DuringRunScene(private val runState: RunState, private val battleManager: BattleManager) : Scene() {
    private val benchBarHeight = cellSize
    private var previousSpeed = 1.0
    private lateinit var updater: Cancellable
    private lateinit var resourceBar: SolidRect
    private lateinit var goldDisplay: Text
    private lateinit var unitCapDisplay: Text
    private lateinit var itemCapDisplay: Text
    private lateinit var fusionCapDisplay: Text
    private lateinit var floorRoomDisplay: Text
    private lateinit var gridContainer: Container
    private val debugMode = false

    override suspend fun Container.sceneInit() {
        battleManager.onBattleComplete = { team -> onBattleComplete(team) }

        previousSpeed = speed
        val root = fixedSizeContainer(viewportWidth, viewportHeight)
        root.run {
            fixedSizeContainer(viewportWidth, resourceBarHeight) {
                resourceBar = solidRect(viewportWidth, resourceBarHeight, Colors.SLATEGREY)
                goldDisplay = text("Gold: 0") {
                    alignLeftToLeftOf(this@fixedSizeContainer, bigMargin)
                }

                unitCapDisplay = text("Units: ${runState.units.size} / ${runState.unitCap}") {
                    alignLeftToRightOf(goldDisplay, bigMargin)
                }

                itemCapDisplay = text("Items: 0 / ${runState.itemCap}") {
                    alignLeftToRightOf(unitCapDisplay, bigMargin)
                }

                fusionCapDisplay = text("Fusions: 0 / ${runState.fusionCap}") {
                    alignLeftToRightOf(itemCapDisplay, bigMargin)
                }

                floorRoomDisplay = text("Room ${runState.floor}:${runState.room}") {
                    alignRightToRightOf(this@fixedSizeContainer, bigMargin)
                }
            }

            drawGrid()

            fixedSizeContainer(viewportWidth, benchBarHeight) {
                alignBottomToBottomOf(root)

                solidRect(viewportWidth, benchBarHeight, Colors.SLATEGREY)
                text("Unit bench goes here")
            }

            initializeHotkeys()
        }

        newBattle()
    }

    private fun Container.drawGrid() {
        gridContainer = fixedSizeContainer(battleWidth, battleHeight) {
            centerXOnStage()
            y = gridYStart.toDouble()

            if (!debugMode) {
                return@fixedSizeContainer
            }

            graphics {
                (0 until gridWidth).forEach { x ->
                    (0 until gridHeight).forEach { y ->
                        roundRect((cellSize - 10).toDouble(), (cellSize - 10).toDouble(), 5.0, fill = Colors["#cccccc"]).xy(cellSize * x + 5, cellSize * y + 5)
                            .alpha(0.25)
                    }
                }
            }
        }
    }

    private fun drawStartButton() {
        sceneView.uiButton(text = "Start Battle") {
            centerOnStage()

            onClick {
                removeFromParent()
                startBattle()
            }
        }
    }

    // TODO: Make configurable in settings
    private fun Container.initializeHotkeys() {
        keys {
            down(Key.SPACE) {
                if (speed == 0.0) {
                    speed = previousSpeed
                } else {
                    previousSpeed = speed
                    speed = 0.0
                }
            }

            down(Key.DOWN) {
                speed /= 2
            }

            down(Key.UP) {
                speed *= 2
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

        floorRoomDisplay.setText("Room ${runState.floor}:${runState.room}")

        println("Now on Floor ${runState.floor} Room ${runState.room}")
    }

    private fun drawLost() {
        val lostText = sceneView.text("You Lost!", textSize = 50.0) {
            centerOnStage()
        }

        sceneView.uiButton(width = 200.0, text = "Return to Menu") {
            centerXOnStage()
            alignTopToBottomOf(lostText, bigMargin)
            onClick {
                sceneContainer.changeTo<MainMenuScene>()
            }
        }
    }

    private fun drawPostBattle() {
        val postBattleContainer = sceneView.container {
            centerOnStage()
        }

        val wonText = postBattleContainer.text("Battle Won!", textSize = 50.0) {
            centerOnStage()
        }

        postBattleContainer.uiButton(text = "Next Battle") {
            centerXOnStage()
            alignTopToBottomOf(wonText)

            onClick {
                postBattleContainer.removeFromParent()
                newBattle()
            }
        }
    }

    private suspend fun newBattle() {
        drawStartButton()
        battleManager.newBattle(gridContainer)
    }

    private suspend fun startBattle() {
        battleManager.start()
        updater = stage.addFixedUpdater(1.timesPerSecond) {
            launchImmediately {
                battleManager.handleTurn()
            }
        }
    }
}
