package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import com.badlogic.gdx.graphics.Color
import ktx.scene2d.vis.visTable
import net.firstrateconcepts.fusionofsouls.util.ext.ui.rectPixmapTexture
import net.firstrateconcepts.fusionofsouls.util.ext.ui.toDrawable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.ScreenView
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar.topBar
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitBench.unitBench

class DuringRunUiView(controller: DuringRunUiController, vm: DuringRunUiViewModel) : ScreenView(controller, vm) {
    override fun init() {
        super.init()

        topBar {
            background(rectPixmapTexture(1, 40, Color.SLATE).toDrawable())
        }.cell(growX = true, height = 40f, row = true)

        visTable {  }.cell(grow = true, row = true)

        unitBench {
            background(rectPixmapTexture(1, 60, Color.SLATE).toDrawable())
        }.cell(growX = true, height = 60f)
    }
}
