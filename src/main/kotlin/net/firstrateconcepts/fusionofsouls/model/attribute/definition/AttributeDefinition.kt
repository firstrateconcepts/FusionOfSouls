package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeValueClamp
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import kotlin.math.roundToInt

interface AttributeDefinition {
    val priority: AttributePriority
    // TODO: Convert shortName into icon
    val shortName: String
    val displayName: String
    val description: String
    val rangeForRandomizer: AttributeRandomRange
    val clamp: AttributeValueClamp

    fun getBaseValue(attrs: AttributesComponent): Float
    fun getDisplayValue(value: Float): String

    fun Float.displayInt() = roundToInt().toString()
    fun Float.displayDecimal(decimals: Int = 2) = "%.${decimals}f".format(this)
    fun Float.displayMultiplier() = "${displayDecimal()}x"
    fun Float.displayPercent(decimals: Int = 1) = "${displayDecimal(decimals)}%"
}

val AttributeType.priority get() = definition.priority
val Attribute.priority get() = type.priority
val AttributeType.shortName get() = definition.shortName
val Attribute.shortName get() = type.shortName
val AttributeType.displayName get() = definition.displayName
val Attribute.displayName get() = type.displayName
val AttributeType.description get() = definition.description
val Attribute.description get() = type.description
val AttributeType.clamp get() = definition.clamp
val Attribute.clamp get() = type.clamp

fun AttributeType.getBaseValue(attrs: AttributesComponent) = definition.getBaseValue(attrs)
fun Attribute.getBaseValue(attrs: AttributesComponent) = type.getBaseValue(attrs)
fun AttributeType.getDisplayValue(value: Float) = definition.getDisplayValue(value)
fun Attribute.getDisplayValue() = type.getDisplayValue(value)
