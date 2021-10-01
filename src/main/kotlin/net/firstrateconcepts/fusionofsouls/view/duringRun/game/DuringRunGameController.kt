package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.NameComponent
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.component.TextureComponent
import net.firstrateconcepts.fusionofsouls.model.event.UnitPositionUpdatedEvent
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class DuringRunGameController : Controller, EventHandler<UnitPositionUpdatedEvent> {
    override val vm = DuringRunGameViewModel()
    override val view = DuringRunGameView(this, vm)

    // TODO: Not where mappers go, just prototyping right now
    private val idMapper = mapperFor<IdComponent>()
    private val nameMapper = mapperFor<NameComponent>()
    private val textureMapper = mapperFor<TextureComponent>()
    private val positionMapper = mapperFor<PositionComponent>()

    fun addNewUnit(entity: Entity): UnitViewModel {
        val unit = UnitViewModel(
            idMapper.get(entity).id,
            nameMapper.get(entity).name,
            textureMapper.get(entity).texture,
            positionMapper.get(entity).position.cpy()
        )
        vm.units.add(unit)
        // TODO: This goes away once we have proper list binding
        view.addUnit(unit)
        return unit
    }

    override suspend fun handle(event: UnitPositionUpdatedEvent) {
        vm.units.find { it.id == event.id }?.apply {
            position(event.position.cpy())
        }
    }
}
