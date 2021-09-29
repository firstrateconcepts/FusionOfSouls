package net.firstrateconcepts.fusionofsouls.view.mainMenu

import com.badlogic.gdx.Application
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.view.settings.SettingsDialogController

class MainMenuController(private val app: Application, private val settingsDialog: SettingsDialogController) : Controller() {
    override val vm = MainMenuViewModel()
    override val view = MainMenuView(this, vm)

    fun newRun() {
        TODO("Not yet implemented")
    }

    fun showSettings() {
        stage?.attachController(settingsDialog, false)
        settingsDialog.show()
    }

    fun exit() = app.exit()
}
