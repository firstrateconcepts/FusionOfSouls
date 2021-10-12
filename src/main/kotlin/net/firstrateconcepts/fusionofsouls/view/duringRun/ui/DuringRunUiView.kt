package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.VisUI
import ktx.actors.onChange
import ktx.scene2d.textButton
import ktx.scene2d.vis.visTable
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindVisible
import net.firstrateconcepts.fusionofsouls.util.ext.ui.rectPixmapTexture
import net.firstrateconcepts.fusionofsouls.util.ext.ui.toDrawable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.ScreenView
import net.firstrateconcepts.fusionofsouls.view.FpsLabel
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar.topBar
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitBench.unitBench

class DuringRunUiView(override val controller: DuringRunUiController, override val vm: DuringRunUiViewModel) : ScreenView(controller, vm) {
    override fun init() {
        super.init()

        val controller = controller
        val vm = vm

        topBar {
            controller.addChild(this.controller)
            background(rectPixmapTexture(1, 40, Color.SLATE).toDrawable())
        }.cell(growX = true, height = 40f, row = true)

        visTable {
            textButton("Start Battle") {
                onChange { controller.startBattle() }
                bindVisible(vm.isStartBattle, true)
            }
            addActor(FpsLabel("FPS: ", VisUI.getSkin()))
        }.cell(grow = true, row = true)

        unitBench {
            controller.addChild(this.controller)
            background(rectPixmapTexture(1, 60, Color.SLATE).toDrawable())
        }.cell(growX = true, height = 60f)
    }
}
