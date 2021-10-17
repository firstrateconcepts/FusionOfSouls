package net.firstrateconcepts.fusionofsouls.service.unit

import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit.UnitController

class UnitCommunicator(eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    private val units = mutableMapOf<Int, UnitController>()

    fun registerUnit(unitId: Int, unit: UnitController) = unit.also { units[unitId] = it }
    fun unregisterUnit(unitId: Int) = units.remove(unitId)
    fun getUnit(id: Int): UnitController? = units[id]
}
