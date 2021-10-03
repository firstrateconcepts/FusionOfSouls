package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.position
import net.firstrateconcepts.fusionofsouls.model.component.texture
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitPositionUpdatedEvent
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.plusAssign
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.removeIf
import java.util.*

class DuringRunGameController(private val eventBus: EventBus, private val engine: PooledEngine) : Controller {
    override val vm = DuringRunGameViewModel()
    override val view = DuringRunGameView(this, vm)

    // TODO: Convert this to query engine each frame, event bus can't handle this event throughput as written
    @HandlesEvent
    suspend fun positionUpdateHandler(event: UnitPositionUpdatedEvent) = onRenderingThread {
        vm.units.get().find { it.id == event.id }?.apply {
            position(event.position.cpy())
        }
    }

    @HandlesEvent
    suspend fun unitActivatedHandler(event: UnitActivatedEvent) = onRenderingThread { engine.findById(event.id)?.also { addNewUnit(it) } }

    @HandlesEvent
    suspend fun unitDeactivatedHandler(event: UnitDeactivatedEvent) = onRenderingThread { removeUnit(event.id) }

    override fun load() {
        eventBus.registerHandlers(this)
    }

    private fun addNewUnit(entity: Entity): UnitViewModel {
        val unit = entity.run { UnitViewModel(id, name, texture, position.cpy()) }
        vm.units += unit
        return unit
    }

    private fun removeUnit(id: UUID) = vm.units.removeIf { it.id == id }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
