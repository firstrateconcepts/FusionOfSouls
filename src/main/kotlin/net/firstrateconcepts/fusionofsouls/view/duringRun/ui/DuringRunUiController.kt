package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.event.NewBattleEvent
import net.firstrateconcepts.fusionofsouls.model.event.battleStarted
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class DuringRunUiController(private val eventBus: EventBus) : Controller {
    override val vm = DuringRunUiViewModel()
    override val view = DuringRunUiView(this, vm)

    @HandlesEvent(NewBattleEvent::class) suspend fun newBattle() = onRenderingThread { vm.isStartBattle(true) }
    @HandlesEvent(BattleStartedEvent::class) suspend fun battleStart() = onRenderingThread { vm.isStartBattle(false) }
    @HandlesEvent(BattleCompletedEvent::class) suspend fun battleComplete() = onRenderingThread { vm.isStartBattle(false) }

    override fun load() {
        eventBus.registerHandlers(this)
        super.load()
    }

    fun startBattle() = eventBus.battleStarted()

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
