package com.runt9.fusionOfSouls.view.duringRun.unitBench

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.widget.VisTable
import com.runt9.fusionOfSouls.benchBarHeight
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.rectPixmapTexture
import com.runt9.fusionOfSouls.util.toDrawable
import com.runt9.fusionOfSouls.viewportWidth
import ktx.scene2d.KTable
import ktx.scene2d.actor

class UnitBench(private val unitBenchDragPane: UnitBenchDragPane) : VisTable(), KTable {
    init {
        setSize(viewportWidth.toFloat(), benchBarHeight.toFloat())
        background(rectPixmapTexture(viewportWidth, benchBarHeight, Color.SLATE).toDrawable())

        actor(unitBenchDragPane).cell(grow = true)

        runState.inactiveUnitAddedListeners += { unitBenchDragPane.addActor(it) }
    }
}
