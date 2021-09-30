package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.UiScreen

abstract class UiScreenController : Controller, UiScreen() {
    override val uiController: Controller get() = this

    override fun dispose() {
        super<Controller>.dispose()
    }
}
