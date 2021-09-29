package net.firstrateconcepts.fusionofsouls.util.framework.ui.view

import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

abstract class ScreenView(controller: Controller, vm: ViewModel) : TableView(controller, vm) {
    override fun init() {
        setSize(stage.width, stage.height)
    }
}
