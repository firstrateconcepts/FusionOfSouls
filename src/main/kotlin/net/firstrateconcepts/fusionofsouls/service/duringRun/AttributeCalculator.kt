package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.PooledEngine
import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getBaseValue
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.attrMods
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.event.AttributeRecalculateNeededEvent
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class AttributeCalculator(private val engine: PooledEngine, private val eventBus: EventBus) : RunService() {
    private val logger = fosLogger()

    @HandlesEvent
    fun handle(event: AttributeRecalculateNeededEvent) = runOnServiceThread {
        logger.info { "Recalculating attributes for unit [${event.unitId}]" }
        engine.findById(event.unitId)?.run { attrs.forEach { it.recalculate(attrs, attrMods) } }
        eventBus.enqueueEventSync(AttributesChangedEvent(event.unitId))
    }

    private fun Attribute.recalculate(attrs: AttributesComponent, mods: Set<AttributeModifier>) {
        val baseValue = getBaseValue(attrs)
        var totalFlat = 0f
        var totalPercent = 0f

        mods.filter { it.type == type }.forEach {
            totalFlat += it.flatModifier
            totalPercent += it.percentModifier
        }

        this((baseValue + totalFlat) * (1 + (totalPercent / 100)))
    }
}
