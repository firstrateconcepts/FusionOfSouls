package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import ktx.async.onRenderingThread
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.event.NewBattleEvent
import net.firstrateconcepts.fusionofsouls.model.event.RunStateUpdated
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu.MenuDialogController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog.UnitDialogController

@Scene2dDsl
fun <S> KWidget<S>.topBar(init: TopBarView.(S) -> Unit = {}) = uiComponent<S, TopBarController, TopBarView>(init = init)

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

    @HandlesEvent(NewBattleEvent::class) suspend fun newBattle() = onRenderingThread { vm.isDuringBattle(false) }
    @HandlesEvent(BattleStartedEvent::class) suspend fun battleStart() = onRenderingThread { vm.isDuringBattle(true) }
    @HandlesEvent(BattleCompletedEvent::class) suspend fun battleComplete() = onRenderingThread { vm.isDuringBattle(false) }

    override fun load() {
        eventBus.registerHandlers(this)
    }

    fun heroButtonClicked() = eventBus.enqueueShowDialog<UnitDialogController>()
    fun bossButtonClicked() = Unit
    fun menuButtonClicked() = eventBus.enqueueShowDialog<MenuDialogController>()

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
