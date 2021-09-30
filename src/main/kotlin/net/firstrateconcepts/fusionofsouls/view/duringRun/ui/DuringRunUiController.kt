package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class DuringRunUiController : Controller {
    override val vm = DuringRunUiViewModel()
    override val view = DuringRunUiView(this, vm)
}
