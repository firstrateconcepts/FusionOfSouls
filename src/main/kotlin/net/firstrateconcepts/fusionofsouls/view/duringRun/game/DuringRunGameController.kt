package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.position
import net.firstrateconcepts.fusionofsouls.model.component.texture
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitPositionUpdatedEvent
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.eventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.plusAssign
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.removeIf
import java.util.*

class DuringRunGameController(private val eventBus: EventBus, engine: PooledEngine) : Controller {
    override val vm = DuringRunGameViewModel()
    override val view = DuringRunGameView(this, vm)

    private val positionUpdateHandler = eventHandler<UnitPositionUpdatedEvent> { event ->
        vm.units.get().find { it.id == event.id }?.apply {
            position(event.position.cpy())
        }
    }

    private val unitActivatedHandler = eventHandler<UnitActivatedEvent> { event -> engine.findById(event.id)?.also { addNewUnit(it) } }
    private val unitDeactivatedHandler = eventHandler<UnitDeactivatedEvent> { event -> removeUnit(event.id) }

    init {
        eventBus.registerHandler(positionUpdateHandler)
        eventBus.registerHandler(unitActivatedHandler)
        eventBus.registerHandler(unitDeactivatedHandler)
    }

    private fun addNewUnit(entity: Entity): UnitViewModel {
        val unit = entity.run { UnitViewModel(id, name, texture, position.cpy()) }
        vm.units += unit
        return unit
    }

    private fun removeUnit(id: UUID) = vm.units.removeIf { it.id == id }

    override fun dispose() {
        eventBus.deregisterHandler(positionUpdateHandler)
        eventBus.deregisterHandler(unitActivatedHandler)
        eventBus.deregisterHandler(unitDeactivatedHandler)
        super.dispose()
    }
}
