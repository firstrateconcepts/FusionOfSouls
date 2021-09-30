package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu

import ktx.actors.onChange
import ktx.scene2d.KTable
import ktx.scene2d.textButton
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.DialogView
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class MenuDialogView(
    override val controller: MenuDialogController,
    vm: ViewModel,
    screenWidth: Int,
    screenHeight: Int
) : DialogView(controller, vm, "Menu", screenWidth, screenHeight) {
    override val widthScale: Float = 0.33f
    override val heightScale: Float = 0.5f

    override fun KTable.initContentTable() {
        val controller = this@MenuDialogView.controller

        textButton("Resume", "round") { onChange { controller.resume() } }.cell(row = true, spaceBottom = 5f)
        textButton("Settings", "round") { onChange { controller.settings() } }.cell(row = true, spaceBottom = 5f)
        textButton("Exit to Main Menu", "round") { onChange { controller.mainMenu() } }.cell(row = true, spaceBottom = 5f)
        textButton("Exit Game", "round") { onChange { controller.exit() } }
    }

    override fun KTable.initButtons() = Unit
}
