package com.runt9.fusionOfSouls.view.duringRun.battleArea

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.layout.FloatingGroup
import com.runt9.fusionOfSouls.battleHeight
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.bigMargin
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.gridXStart
import com.runt9.fusionOfSouls.gridYStart
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.squarePixmap
import ktx.actors.plusAssign
import ktx.actors.setPosition
import ktx.scene2d.KGroup
import ktx.scene2d.actor
import ktx.scene2d.vis.flowGroup

class BattleArea(private val dragPane: UnitGridDragPane) : FloatingGroup(), KGroup {
    init {
        toBack()
        setPosition(gridXStart, gridYStart)
        width = battleWidth.toFloat() - bigMargin
        height = battleHeight.toFloat() - bigMargin

        if (debug) {
            flowGroup(spacing = 10f) {
                width = battleWidth.toFloat() - bigMargin
                height = battleHeight.toFloat() - bigMargin

                repeat(gridWidth * gridHeight) { addActor(squarePixmap(cellSize - 10, Color.DARK_GRAY)) }
            }
        }

        actor(dragPane)
    }

    fun drawUnits() {
        runState.battleContext.playerTeam.forEach {
            dragPane += it
            it.toFront()
        }

        runState.battleContext.hero.let {
            dragPane += it
            it.toFront()
        }

        runState.battleContext.enemies.forEach {
            this += it
            it.setInitialPosition()
            it.toFront()
        }
    }
}
