package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.clamp
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getBaseValue
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.primary.PrimaryAttributeDefinition
import net.firstrateconcepts.fusionofsouls.model.component.attrMods
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.get
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class AttributeService(private val eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    private val logger = fosLogger()
    private val callbacks = mutableMapOf<Int, MutableMap<AttributeType, MutableList<(Entity, Attribute) -> Unit>>>()

    fun onChange(unitId: Int, attr: AttributeType, callback: (Entity, Attribute) -> Unit) =
        callbacks.computeIfAbsent(unitId) { mutableMapOf() }.computeIfAbsent(attr) { mutableListOf() }.add(callback)

    fun removeOnChangeCallback(unitId: Int, attr: AttributeType, callback: (Entity, Attribute) -> Unit) = callbacks[unitId]?.get(attr)?.remove(callback)

    fun recalculateAll(entity: Entity) {
        logger.info { "Recalculating attributes for unit [${entity.id}]" }
        entity.attrs.forEach { it.recalculate(entity) }
    }

    fun addModifiers(entity: Entity, modifiers: List<AttributeModifier>) = modifyModifiers(entity, modifiers) { addAll(it) }
    fun removeModifiers(entity: Entity, modifiers: List<AttributeModifier>) = modifyModifiers(entity, modifiers) { removeAll(it) }

    private fun Attribute.recalculate(entity: Entity) {
        val attrs = entity.attrs
        val mods = entity.attrMods

        val baseValue = getBaseValue(attrs)
        var totalFlat = 0f
        var totalPercent = 0f

        mods.filter { it.type == type }.forEach {
            totalFlat += it.flatModifier
            totalPercent += it.percentModifier
        }

        val newValue = ((baseValue + totalFlat) * (1 + (totalPercent / 100))).clamp(clamp)
        if (newValue != this()) {
            this(newValue)
            callbacks[entity.id]?.get(type)?.forEach { it.invoke(entity, this) }
            if (this.type.definition is PrimaryAttributeDefinition) {
                this.type.definition.affects.forEach { attrs[it].recalculate(entity) }
            }
        }
    }

    private fun Float.clamp(range: AttributeValueClamp) = when {
        range.min != null && range.min > this -> range.min
        range.max != null && range.max < this -> range.max
        else -> this
    }

    private fun modifyModifiers(entity: Entity, modifiers: List<AttributeModifier>, action: MutableSet<AttributeModifier>.(List<AttributeModifier>) -> Unit) {
        modifiers.groupBy(AttributeModifier::type).forEach { (type, modifiers) ->
            entity.attrMods.action(modifiers)
            entity.attrs[type].recalculate(entity)
        }
    }
}
