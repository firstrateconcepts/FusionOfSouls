package com.runt9.fusionOfSouls.scene

import com.runt9.fusionOfSouls.battleHeight
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.gridYStart
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.BattleUnitManager
import com.runt9.fusionOfSouls.service.GridService
import com.runt9.fusionOfSouls.service.PathService
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
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alpha
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.fixedSizeContainer
import com.soywiz.korge.view.graphics
import com.soywiz.korge.view.roundRect
import com.soywiz.korge.view.solidRect
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.lang.Cancellable
import com.soywiz.korio.lang.cancel


class DuringRunScene(private val runState: RunState, private val gridService: GridService, private val pathService: PathService, private val unitManager: BattleUnitManager) : Scene() {
    private val benchBarHeight = cellSize
    private var previousSpeed = 1.0
    private lateinit var battleManager: BattleManager
    private lateinit var updater: Cancellable

    override suspend fun Container.sceneInit() {
        battleManager = BattleManager(runState, gridService, pathService, unitManager, coroutineContext) { str -> onBattleComplete(str) }

        previousSpeed = speed
        val root = fixedSizeContainer(viewportWidth, viewportHeight)
        root.run {
            solidRect(viewportWidth, resourceBarHeight, Colors.SLATEGREY) {
                text("Resource Info Goes Here (gold, units, loot level, etc)")
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
        fixedSizeContainer(battleWidth, battleHeight) {
            centerXOnStage()
            y = gridYStart.toDouble()

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

    private fun Container.drawStartButton() {
        uiButton(text = "Start Battle") {
            centerOnStage()

            onClick {
                removeFromParent()
                battleManager.start()
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

    private suspend fun Container.onBattleComplete(str: String) {
        // Show post-battle rewards
        // After post-battle rewards, start new battle
        updater.cancel()
        newBattle()
    }

    private suspend fun Container.newBattle() {
        drawStartButton()
        battleManager.newBattle()
        unitManager.playerTeam.forEach { it.addTo(this) }
        unitManager.enemyTeam.forEach { it.addTo(this) }

        updater = addFixedUpdater(1.timesPerSecond) {
            println("Fixed updater")
            launchImmediately {
                battleManager.handleTurn()
            }
        }
    }
}
