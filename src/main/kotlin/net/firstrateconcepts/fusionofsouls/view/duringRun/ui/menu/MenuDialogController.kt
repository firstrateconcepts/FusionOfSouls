package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu

import com.badlogic.gdx.Graphics
import net.firstrateconcepts.fusionofsouls.model.event.enqueueChangeScreen
import net.firstrateconcepts.fusionofsouls.model.event.enqueueExitRequest
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.emptyViewModel
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController
import net.firstrateconcepts.fusionofsouls.view.settings.SettingsDialogController

class MenuDialogController(private val eventBus: EventBus, graphics: Graphics) : DialogController() {
    override val vm = emptyViewModel()
    override val view = MenuDialogView(this, vm, graphics.width, graphics.height)

    fun resume() = hide()
    fun settings() = eventBus.enqueueShowDialog<SettingsDialogController>()
    fun mainMenu() {
        eventBus.enqueueChangeScreen<MainMenuScreenController>()
        hide()
    }
    fun exit() = eventBus.enqueueExitRequest()
}
