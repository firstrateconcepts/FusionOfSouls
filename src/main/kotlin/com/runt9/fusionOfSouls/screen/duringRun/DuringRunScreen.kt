package com.runt9.fusionOfSouls.screen.duringRun

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.basicMargin
import com.runt9.fusionOfSouls.battleHeight
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.bigMargin
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.gridXStart
import com.runt9.fusionOfSouls.gridYStart
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.screen.FosScreen
import com.runt9.fusionOfSouls.screen.MainMenuScreen
import com.runt9.fusionOfSouls.screen.duringRun.charDialog.CharacterDialog
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.fosVisTable
import com.runt9.fusionOfSouls.util.rectPixmapTexture
import com.runt9.fusionOfSouls.util.squarePixmap
import com.runt9.fusionOfSouls.util.toDrawable
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import com.soywiz.korio.async.launch
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.setPosition
import ktx.async.KtxAsync
import ktx.async.interval
import ktx.scene2d.StageWidget
import ktx.scene2d.actors
import ktx.scene2d.vis.floatingGroup
import ktx.scene2d.vis.flowGroup
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton


// TODO: Ensure all user actions are disabled during battle
class DuringRunScreen(private val game: FosGame, private val battleManager: BattleManager, override val stage: Stage) : FosScreen {
    private val benchBarHeight = cellSize
    private lateinit var updater: Timer.Task
    private lateinit var resourceBar: VisTable
    private lateinit var goldDisplay: VisLabel
    private lateinit var unitCapDisplay: VisLabel
    private lateinit var runeCapDisplay: VisLabel
    private lateinit var fusionCapDisplay: VisLabel
    private lateinit var floorRoomDisplay: VisLabel
    private lateinit var heroButton: VisTextButton
    private lateinit var gridContainer: Group
    private val heroDialog by lazy { CharacterDialog(runState.hero.name) }

    override fun show() {
        battleManager.onBattleComplete = { team -> onBattleComplete(team) }
        stage.actors {
            drawGrid()
            drawTopBar()
            drawUnitBar()
        }
        KtxAsync.launch { newBattle() }
    }

    private fun StageWidget.drawTopBar() {
        resourceBar = visTable(true) {
            defaults().expand().left().padLeft(basicMargin.toFloat())
            y = viewportHeight - resourceBarHeight.toFloat()
            setSize(viewportWidth.toFloat(), resourceBarHeight.toFloat())
            background(rectPixmapTexture(viewportWidth, resourceBarHeight, Color.SLATE).toDrawable())

            visTable(true) {
                defaults().expand().center()
                goldDisplay = visLabel("Gold: ${runState.gold}")
                unitCapDisplay = visLabel("Units: ${runState.units.size} / ${runState.unitCap}")
                runeCapDisplay = visLabel("Runes: ${runState.hero.runes.size} / ${runState.runeCap}")
                fusionCapDisplay = visLabel("Fusions: ${runState.hero.fusions.size} / ${runState.fusionCap}")
            }

            defaults().expand().right().padRight(basicMargin.toFloat())
            visTable(true) {
                defaults().expand().center()
                heroButton = visTextButton("Hero") {
                    setOrigin(Align.center)
                    scaleBy(-0.33f)
                    isTransform = true
                    onClick {
                        heroDialog.show(this@DuringRunScreen.stage)
                    }
                }
                floorRoomDisplay = visLabel("Room ${runState.floor}:${runState.room}")
            }
        }
    }

    private fun StageWidget.drawGrid() {
        gridContainer = floatingGroup {
            toBack()
            setPosition(gridXStart, gridYStart)
            width = battleWidth.toFloat() - bigMargin
            height = battleHeight.toFloat() - bigMargin
            flowGroup(spacing = 10f) {
                width = battleWidth.toFloat() - bigMargin
                height = battleHeight.toFloat() - bigMargin

                repeat(gridWidth * gridHeight) { addActor(squarePixmap(cellSize - 10, Color.DARK_GRAY)) }
            }
        }
    }

    private fun StageWidget.drawUnitBar() {
        visTable {
            setSize(viewportWidth.toFloat(), benchBarHeight.toFloat())
            background(rectPixmapTexture(viewportWidth, benchBarHeight, Color.SLATE).toDrawable())
            add("Unit bench goes here")
        }
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

        floorRoomDisplay.setText("Room ${runState.floor}:${runState.room}")

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
        battleManager.newBattle(gridContainer)
    }

    private suspend fun startBattle() {
        battleManager.start()
        updater = interval(1f) {
            KtxAsync.launch { battleManager.handleTurn() }
        }
    }
}
