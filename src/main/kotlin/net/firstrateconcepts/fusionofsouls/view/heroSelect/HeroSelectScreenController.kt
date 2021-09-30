package net.firstrateconcepts.fusionofsouls.view.heroSelect

import net.firstrateconcepts.fusionofsouls.model.event.enqueueChangeScreen
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.UiScreenController
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.emptyViewModel
import net.firstrateconcepts.fusionofsouls.view.duringRun.DuringRunScreen
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController

class HeroSelectScreenController(private val eventBus: EventBus) : UiScreenController() {
    override val vm = emptyViewModel()
    override val view = HeroSelectView(this, vm)

    fun back() = eventBus.enqueueChangeScreen<MainMenuScreenController>()
    fun startRun() = eventBus.enqueueChangeScreen<DuringRunScreen>()
}
