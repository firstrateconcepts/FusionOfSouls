package com.runt9.fusionOfSouls.model.unit.attribute

import kotlin.properties.Delegates

typealias ValueChangeListener = (Double) -> Unit

abstract class Attribute {
    var value: Double by Delegates.observable(0.0) { _, _, _ -> valueChanged() }
        private set

    private val modifiers = mutableListOf<AttributeModifier>()
    val valueListeners = mutableListOf<ValueChangeListener>()

    protected abstract fun getBase(): Double

    fun recalculate() {
        var flatModifier = 0.0
        var percentModifier = 0.0

        modifiers.forEach { mod ->
            flatModifier += mod.flatModifier
            percentModifier += mod.percentModifier
        }

        value = (getBase() + flatModifier) * (1 + percentModifier)
    }

    private fun valueChanged() {
        valueListeners.forEach { it.invoke(value) }
    }

    fun addModifier(flatModifier: Double = 0.0, percentModifier: Double = 0.0) {
        modifiers.add(AttributeModifier(flatModifier, percentModifier))
        recalculate()
    }

    fun addModifier(modifier: AttributeModifier) {
        modifiers.add(modifier)
        recalculate()
    }

    fun removeModifier(modifier: AttributeModifier) {
        modifiers.remove(modifier)
        recalculate()
    }
}

data class AttributeModifier(val flatModifier: Double = 0.0, val percentModifier: Double = 0.0)

operator fun Attribute.plus(value: Double) = this.value + value
operator fun Attribute.plus(attr: Attribute) = this + attr.value
operator fun Attribute.minus(value: Double) = this.value - value
operator fun Attribute.minus(attr: Attribute) = this - attr.value
operator fun Attribute.times(value: Double) = this.value * value
operator fun Attribute.times(attr: Attribute) = this * attr.value
operator fun Attribute.div(value: Double) = this.value / value
operator fun Attribute.div(attr: Attribute) = this / attr.value
fun Attribute.sqrt() = kotlin.math.sqrt(this.value)
