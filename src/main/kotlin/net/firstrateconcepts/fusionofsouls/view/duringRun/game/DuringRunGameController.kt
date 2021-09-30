package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class DuringRunGameController : Controller {
    override val vm = DuringRunGameViewModel()
    override val view = DuringRunGameView(this, vm)
}
