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

operator fun AttributeModifiersComponent.get(type: AttributeType) = modifiers.filter { it.type == type }
