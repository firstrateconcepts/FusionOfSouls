package net.firstrateconcepts.fusionofsouls.view.mainMenu

import com.badlogic.gdx.Application
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.UiScreenController
import net.firstrateconcepts.fusionofsouls.util.framework.ui.emptyViewModel
import net.firstrateconcepts.fusionofsouls.view.heroSelect.HeroSelectScreenController
import net.firstrateconcepts.fusionofsouls.view.settings.SettingsDialogController

class MainMenuScreenController(private val app: Application, private val eventBus: EventBus) : UiScreenController() {
    override val vm = emptyViewModel()
    override val view = MainMenuView(this, vm)

    fun newRun() = eventBus.enqueueChangeScreen<HeroSelectScreenController>()
    fun showSettings() = showDialog<SettingsDialogController>()
    fun exit() = app.exit()
}
