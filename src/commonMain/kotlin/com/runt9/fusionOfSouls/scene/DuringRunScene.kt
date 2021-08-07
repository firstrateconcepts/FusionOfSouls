package com.runt9.fusionOfSouls.scene

import com.runt9.fusionOfSouls.battleHeight
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.gridHeight
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alpha
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.fixedSizeContainer
import com.soywiz.korge.view.graphics
import com.soywiz.korge.view.roundRect
import com.soywiz.korge.view.solidRect
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors

class DuringRunScene : Scene() {
    private val resourceBarHeight = 20
    private val benchBarHeight = cellSize

    override suspend fun Container.sceneInit() {
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
        }
    }

    private fun Container.drawGrid() {
        fixedSizeContainer(battleWidth, battleHeight) {
            centerXOnStage()
            y = (resourceBarHeight + 10).toDouble()

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
}
