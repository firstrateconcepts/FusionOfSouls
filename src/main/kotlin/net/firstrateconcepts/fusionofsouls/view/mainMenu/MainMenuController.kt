package net.firstrateconcepts.fusionofsouls.view.mainMenu

import com.badlogic.gdx.Application
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Controller

class MainMenuController(private val app: Application) : Controller {
    override val vm = MainMenuViewModel()
    override val view = MainMenuView(this, vm)

    fun newRun() {
        TODO("Not yet implemented")
    }

    fun showSettings() {
        TODO("Not yet implemented")
    }

    fun exit() = app.exit()
}
