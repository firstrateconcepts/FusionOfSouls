package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getBaseValue
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.attrMods
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.AttributesChangedEvent
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class AttributeCalculator(private val eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    private val logger = fosLogger()

    fun recalculate(entity: Entity) {
        logger.info { "Recalculating attributes for unit [${entity.id}]" }
        entity.attrs.forEach { it.recalculate(entity.attrs, entity.attrMods) }
        eventBus.enqueueEventSync(AttributesChangedEvent(entity.id))
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
