package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import ktx.async.onRenderingThread
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu.MenuDialogController

@Scene2dDsl
inline fun <S> KWidget<S>.topBar(init: TopBarView.(S) -> Unit = {}) = uiComponent<S, TopBarController, TopBarView>(init)

class TopBarController(private val eventBus: EventBus) : Controller {
    override val vm = TopBarViewModel()
    override val view = TopBarView(this, vm)

    @HandlesEvent
    suspend fun runStateHandler(event: RunStateUpdated) = onRenderingThread {
        val newState = event.newState
        vm.apply {
            gold(newState.gold)
            unitCap(newState.unitCap)
            floor(newState.floor)
            room(newState.room)
        }
    }

    @HandlesEvent
    suspend fun runStatusHandler(event: RunStatusChanged) = onRenderingThread { vm.isDuringBattle(event.newStatus == BattleStatus.DURING_BATTLE) }

    override fun load() {
        eventBus.registerHandlers(this)
    }

    fun heroButtonClicked() = Unit
    fun bossButtonClicked() = Unit
    fun menuButtonClicked() = eventBus.enqueueShowDialog<MenuDialogController>()

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
