package net.firstrateconcepts.fusionofsouls.view.mainMenu

import net.firstrateconcepts.fusionofsouls.util.framework.ui.Controller

class MainMenuController : Controller {
    override val vm = MainMenuViewModel()
    override val view = MainMenuView(this, vm)
}
