package net.firstrateconcepts.fusionofsouls.view.duringRun.ui

import com.badlogic.gdx.utils.Disposable
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
    private val children = mutableListOf<Controller>()

    @HandlesEvent(NewBattleEvent::class) suspend fun newBattle() = onRenderingThread { vm.isStartBattle(true) }
    @HandlesEvent(BattleStartedEvent::class) suspend fun battleStart() = onRenderingThread { vm.isStartBattle(false) }
    @HandlesEvent(BattleCompletedEvent::class) suspend fun battleComplete() = onRenderingThread { vm.isStartBattle(false) }

    override fun load() {
        eventBus.registerHandlers(this)
        super.load()
    }

    fun startBattle() = eventBus.battleStarted()

    override fun dispose() {
        children.forEach(Disposable::dispose)
        eventBus.unregisterHandlers(this)
        super.dispose()
    }

    fun addChild(controller: Controller) = children.add(controller)
}
