package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType

class AttributeModifiersComponent : Component, Iterable<AttributeModifier> {
    val modifiers = mutableSetOf<AttributeModifier>()

    override fun iterator() = modifiers.iterator()
}

val attributeModifiersMapper = mapperFor<AttributeModifiersComponent>()
val Entity.attrMods get() = this[attributeModifiersMapper]!!.modifiers
fun Entity.addModifier(type: AttributeType, flatModifier: Float = 0f, percentModifier: Float = 0f, isTemporary: Boolean = false) =
    attrMods.add(AttributeModifier(type, flatModifier, percentModifier, isTemporary))

operator fun AttributeModifiersComponent.get(type: AttributeType) = modifiers.filter { it.type == type }
