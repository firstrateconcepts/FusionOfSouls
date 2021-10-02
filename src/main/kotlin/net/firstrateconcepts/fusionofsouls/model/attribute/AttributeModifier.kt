package net.firstrateconcepts.fusionofsouls.model.attribute

data class AttributeModifier(val type: AttributeType, val flatModifier: Float = 0f, val percentModifier: Float = 0f, val isTemporary: Boolean = false)
