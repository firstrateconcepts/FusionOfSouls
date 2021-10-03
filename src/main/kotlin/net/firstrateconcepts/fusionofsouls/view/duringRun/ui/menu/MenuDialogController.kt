package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu

import com.badlogic.gdx.Graphics
import net.firstrateconcepts.fusionofsouls.model.event.enqueueChangeScreen
import net.firstrateconcepts.fusionofsouls.model.event.enqueueExitRequest
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.model.event.pauseGame
import net.firstrateconcepts.fusionofsouls.model.event.resumeGame
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController
import net.firstrateconcepts.fusionofsouls.view.settings.SettingsDialogController

class MenuDialogController(private val eventBus: EventBus, graphics: Graphics, private val runStateService: RunStateService) : DialogController() {
    override val vm = MenuDialogViewModel()
    override val view = MenuDialogView(this, vm, graphics.width, graphics.height)

    override fun load() {
        runStateService.load().apply { vm.runSeed(seed) }
        eventBus.pauseGame()
    }

    fun resume() {
        eventBus.resumeGame()
        hide()
    }
    fun settings() = eventBus.enqueueShowDialog<SettingsDialogController>()
    fun mainMenu() {
        eventBus.enqueueChangeScreen<MainMenuScreenController>()
        hide()
    }
    fun exit() = eventBus.enqueueExitRequest()
}
