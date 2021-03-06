package net.firstrateconcepts.fusionofsouls.view.mainMenu

import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.vis.visLabel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.ScreenView

class MainMenuView(override val controller: MainMenuScreenController, override val vm: ViewModel) : ScreenView(controller, vm) {
    override fun init() {
        super.init()

        visLabel("Fusion of Souls", "title").cell(row = true, spaceBottom = 20f)

        textButton("New Run", "round") {
            onClick { this@MainMenuView.controller.newRun() }
        }.cell(row = true, spaceBottom = 10f)

        textButton("Settings", "round") {
            onClick { this@MainMenuView.controller.showSettings() }
        }.cell(row = true, spaceBottom = 10f)

        textButton("Quit", "round") {
            onClick { this@MainMenuView.controller.exit() }
        }.cell(row = true)
    }
}
