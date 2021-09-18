package com.runt9.fusionOfSouls.screen.duringRun

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.kotcrab.vis.ui.layout.DragPane
import com.kotcrab.vis.ui.widget.Draggable
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
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.hero.Hero
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.screen.FosScreen
import com.runt9.fusionOfSouls.screen.MainMenuScreen
import com.runt9.fusionOfSouls.screen.duringRun.charDialog.CharacterDialog
import com.runt9.fusionOfSouls.screen.duringRun.postBattle.PostBattleDialog
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.UnitGenerator
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.fosVisTable
import com.runt9.fusionOfSouls.util.rectPixmapTexture
import com.runt9.fusionOfSouls.util.squarePixmap
import com.runt9.fusionOfSouls.util.toDrawable
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import com.soywiz.korio.async.launch
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.setPosition
import ktx.async.KtxAsync
import ktx.async.interval
import ktx.scene2d.KStack
import ktx.scene2d.StageWidget
import ktx.scene2d.actor
import ktx.scene2d.actors
import ktx.scene2d.image
import ktx.scene2d.stack
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.floatingGroup
import ktx.scene2d.vis.flowGroup
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton


// TODO: Ensure all user actions are disabled during battle
class DuringRunScreen(private val game: FosGame, private val battleManager: BattleManager, private val unitGenerator: UnitGenerator, override val stage: Stage) : FosScreen {
    private val debug = false

    private val benchBarHeight = cellSize
    private lateinit var updater: Timer.Task
    private lateinit var resourceBar: VisTable
    private lateinit var goldDisplay: VisLabel
    private lateinit var unitCapDisplay: VisLabel
    private lateinit var floorRoomDisplay: VisLabel
    private lateinit var heroButton: VisTextButton
    private lateinit var gridContainer: Group
    private lateinit var playerUnitGrid: KVisTable

    override fun show() {
        battleManager.onBattleComplete = { team -> onBattleComplete(team) }
        stage.actors {
            drawGrid()
            drawTopBar()
            drawUnitBar()
        }
        newBattle()
    }

    private fun StageWidget.drawTopBar() {
        resourceBar = visTable(true) {
            defaults().expand().left().padLeft(basicMargin.toFloat())
            y = viewportHeight - resourceBarHeight.toFloat()
            setSize(viewportWidth.toFloat(), resourceBarHeight.toFloat())
            background(rectPixmapTexture(viewportWidth, resourceBarHeight, Color.SLATE).toDrawable())

            visTable(true) {
                defaults().expand().center()
                val goldText = { "Gold: ${runState.gold}" }
                goldDisplay = visLabel(goldText()) {
                    runState.goldListeners.add { setText(goldText()) }
                }
                unitCapDisplay = visLabel("Units: ${runState.activeUnits.size} / ${runState.unitCap}")
            }

            defaults().expand().right().padRight(basicMargin.toFloat())
            visTable(true) {
                defaults().expand().center()
                heroButton = visTextButton("Hero") {
                    setOrigin(Align.center)
                    scaleBy(-0.33f)
                    isTransform = true
                    onClick {
                        val heroDialog = CharacterDialog(runState.hero.name)
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
            if (this@DuringRunScreen.debug) {
                flowGroup(spacing = 10f) {
                    width = battleWidth.toFloat() - bigMargin
                    height = battleHeight.toFloat() - bigMargin

                    repeat(gridWidth * gridHeight) { addActor(squarePixmap(cellSize - 10, Color.DARK_GRAY)) }
                }
            }

            playerUnitGrid = visTable {
                align(Align.left)
                width = (battleWidth.toFloat() - bigMargin) * (4f / 14f)
                height = battleHeight.toFloat() - bigMargin

                repeat(gridHeight) {
                    row()
                    repeat(4) {
                        stack {
                            squarePixmap(cellSize - 10, Color.CLEAR)
                        }.cell(space = 10f)
                    }
                }
            }

//            playerUnitGrid = UnitGridDragPane(paneTable)
//            actor(playerUnitGrid)
        }
    }

    private fun StageWidget.drawUnitBar() {
        val drawUnits: KVisTable.() -> Unit = {
            clearChildren()
            runState.inactiveUnits.map { it.unitImage }.forEach {
                image(it).cell(align = Align.center, padLeft = 5f, padRight = 7f)
            }
        }

        visTable {
            setSize(viewportWidth.toFloat(), benchBarHeight.toFloat())
            background(rectPixmapTexture(viewportWidth, benchBarHeight, Color.SLATE).toDrawable())
            stack {
                flowGroup(spacing = 2f) {
                    repeat(15) {
                        actor(squarePixmap(benchBarHeight, Color.LIGHT_GRAY))
                    }
                }
                visTable {
                    align(Align.left)
                    drawUnits()

                    runState.unitListeners.add {
                        drawUnits()
                    }
                }
            }
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
//        playerUnitGrid.initialized = false

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
        val dialog = PostBattleDialog(unitGenerator) { newBattle() }
        dialog.show(this@DuringRunScreen.stage)
    }

    private fun newBattle() {
        drawStartButton()
        battleManager.newBattle(gridContainer, playerUnitGrid)
    }

    private suspend fun startBattle() {
        battleManager.start()
        updater = interval(1f) {
            KtxAsync.launch { battleManager.handleTurn() }
        }
    }

    class UnitGridDragPane(paneTable: KVisTable) : DragPane(paneTable) {
        var initialized = false

        init {
            width = (battleWidth.toFloat() - bigMargin) * (4f / 14f)
            height = battleHeight.toFloat() - bigMargin
//            align(Align.bottomLeft)
            draggable = Draggable()
//            draggable.isInvisibleWhenDragged = true

//            runState.activeUnits.map { BattleUnit(it, Team.PLAYER) }.forEach(this::addActor)
        }

        override fun addActor(actor: Actor) {
            println("Add actor")
            ((this.actor as KVisTable).cells[24].actor as KStack).add(actor)
            doOnAdd(actor)
        }

        override fun addActorAt(index: Int, actor: Actor) {
            println("Add actor at $index")
//            super.addActorAt(index, actor)
            doOnAdd(actor)
        }

        override fun addActorBefore(actorBefore: Actor, actor: Actor) {
            println("Add actor before ${actorBefore}")
            if (actorBefore is KStack) {
                actorBefore.add(actor)
                doOnAdd(actor)
            }
        }

        override fun addActorAfter(actorAfter: Actor, actor: Actor) {
            println("Add actor before ${actorAfter}")
            if (actorAfter is KStack) {
                actorAfter.add(actor)
                doOnAdd(actor)
            }
        }

        override fun accept(actor: Actor) = if (!(actor is BattleUnit && actor.unit is Hero) && runState.activeUnits.size >= runState.unitCap) {
            false
        } else {
            super.accept(actor)
        }

        override fun doOnAdd(actor: Actor) {
            super.doOnAdd(actor)
            if (initialized) {
                val unit = actor as BattleUnit
                if (unit.unit is Hero) {
                    return
                }

                runState.apply {
                    inactiveUnits.remove(unit.unit)
                    activeUnits.add(unit.unit as BasicUnit)
                }
                notify(UnitCountUpdateEvent(this), false)
            }
        }
    }

    class UnitCountUpdateEvent(target: Actor) : Event() {
        init {
            bubbles = true
            setTarget(target)
        }
    }
}
