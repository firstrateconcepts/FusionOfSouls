package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.model.RunStatus
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.eventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu.MenuDialogController

@Scene2dDsl
inline fun <S> KWidget<S>.topBar(init: TopBarView.(S) -> Unit = {}) = uiComponent<S, TopBarController, TopBarView>(init)

class TopBarController(private val eventBus: EventBus) : Controller {
    override val vm = TopBarViewModel()
    override val view = TopBarView(this, vm)

    private val runStateHandler = eventHandler<RunStateUpdated> {
        val newState = it.newState
        vm.apply {
            gold(newState.gold)
            unitCap(newState.unitCap)
            floor(newState.floor)
            room(newState.room)
        }
    }

    private val runStatusHandler = eventHandler<RunStatusChanged> {
        vm.isDuringBattle(it.newStatus == RunStatus.DURING_BATTLE)
    }

    override fun load() {
        eventBus.registerHandler(runStateHandler)
        eventBus.registerHandler(runStatusHandler)
    }

    fun heroButtonClicked() = Unit
    fun bossButtonClicked() = Unit
    fun menuButtonClicked() = eventBus.enqueueShowDialog<MenuDialogController>()

    override fun dispose() {
        eventBus.deregisterHandler(runStateHandler)
        eventBus.deregisterHandler(runStatusHandler)
        super.dispose()
    }
}
