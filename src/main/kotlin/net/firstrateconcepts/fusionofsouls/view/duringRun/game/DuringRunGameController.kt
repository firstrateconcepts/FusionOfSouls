package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Disposable
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.rotation
import net.firstrateconcepts.fusionofsouls.model.component.texture
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.plusAssign
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.removeIf
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit.UnitViewModel

class DuringRunGameController(private val eventBus: EventBus, private val engine: AsyncPooledEngine) : Controller {
    override val vm = DuringRunGameViewModel()
    override val view = DuringRunGameView(this, vm)
    private val children = mutableListOf<Controller>()

    @HandlesEvent
    suspend fun unitActivatedHandler(event: UnitActivatedEvent) = engine.withUnit(event.unitId) { onRenderingThread { addNewUnit(this@withUnit) } }

    @HandlesEvent
    suspend fun unitDeactivatedHandler(event: UnitDeactivatedEvent) = onRenderingThread { removeUnit(event.unitId) }

    override fun load() {
        eventBus.registerHandlers(this)
    }

    private fun addNewUnit(entity: Entity): UnitViewModel {
        val unit = entity.run { UnitViewModel(id, name, texture, currentPosition.cpy(), rotation) }
        vm.units += unit
        return unit
    }

    private fun removeUnit(id: Int) = vm.units.removeIf { it.id == id }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }

    fun clearChildren() {
        children.forEach(Disposable::dispose)
        children.clear()
    }

    fun addChild(controller: Controller) = children.add(controller)
}
