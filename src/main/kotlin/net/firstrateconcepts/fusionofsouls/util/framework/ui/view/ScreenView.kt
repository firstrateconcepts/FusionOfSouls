package net.firstrateconcepts.fusionofsouls.util.framework.ui.view

import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

abstract class ScreenView(controller: Controller, vm: ViewModel) : TableView(controller, vm) {
    override fun init() {
        setSize(stage.width, stage.height)
    }
}
