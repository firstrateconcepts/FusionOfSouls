package net.firstrateconcepts.fusionofsouls.view.mainMenu

import net.firstrateconcepts.fusionofsouls.model.event.enqueueChangeScreen
import net.firstrateconcepts.fusionofsouls.model.event.enqueueExitRequest
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.UiScreenController
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.emptyViewModel
import net.firstrateconcepts.fusionofsouls.view.heroSelect.HeroSelectScreenController
import net.firstrateconcepts.fusionofsouls.view.settings.SettingsDialogController

class MainMenuScreenController(private val eventBus: EventBus) : UiScreenController() {
    override val vm = emptyViewModel()
    override val view = MainMenuView(this, vm)

    fun newRun() = eventBus.enqueueChangeScreen<HeroSelectScreenController>()
    fun showSettings() = eventBus.enqueueShowDialog<SettingsDialogController>()
    fun exit() = eventBus.enqueueExitRequest()
}
