package com.runt9.fusionOfSouls.model.unit.attribute

import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.random.nextInt

typealias ValueChangeListener = (Double) -> Unit

interface AttributeType<A : Attribute, T : Attributes<A>> {
    val unitAttrSelection: GameUnit.() -> T
    val displayName: String
    val attrsAttrSelection: T.() -> A
    val attrRandomizer: AttributeModifierRandomizer
    val valueDisplayer: (Double) -> String
}

abstract class Attribute(val type: AttributeType<*, *>) {
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

        value = (getBase() + flatModifier) * (1 + (percentModifier / 100))
    }

    private fun valueChanged() {
        valueListeners.forEach { it(value) }
    }

    fun addListener(applyImmediately: Boolean = true, listener: ValueChangeListener) {
        valueListeners.add(listener)
        if (applyImmediately) {
            listener(value)
        }
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

    fun purgeTemporaryModifiers() {
        modifiers.removeAll { it.isTemporary }
    }

    fun displayValue() = type.valueDisplayer(value)
}

data class AttributeModifier(val flatModifier: Double = 0.0, val percentModifier: Double = 0.0, val isTemporary: Boolean = false)

class AttributeModifierRandomizer(private val flatMin: Number, private val flatMax: Number, private val percentRange: IntRange = 10..15) {
    fun getRandom(rarity: Rarity, coinFlip: Boolean = Random.nextBoolean()): AttributeModifier {
        val multiplier = rarity.ordinal + 1

        return if (coinFlip) {
            AttributeModifier(flatModifier = (Random.nextDouble(flatMin.toDouble(), flatMax.toDouble()) * multiplier).roundDecimalPlaces(2))
        } else {
            AttributeModifier(percentModifier = (Random.nextInt(percentRange) * multiplier).toDouble())
        }
    }
}

operator fun Attribute.plus(value: Double) = this.value + value
operator fun Attribute.plus(attr: Attribute) = this + attr.value
operator fun Attribute.minus(value: Double) = this.value - value
operator fun Attribute.minus(attr: Attribute) = this - attr.value
operator fun Attribute.times(value: Double) = this.value * value
operator fun Attribute.times(attr: Attribute) = this * attr.value
operator fun Attribute.div(value: Double) = this.value / value
operator fun Attribute.div(attr: Attribute) = this / attr.value
fun Attribute.sqrt() = kotlin.math.sqrt(this.value)
