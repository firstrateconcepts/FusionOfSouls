package net.firstrateconcepts.fusionofsouls.model.attribute.definition

import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeRandomRange
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import kotlin.math.roundToInt

sealed interface AttributeDefinition {
    val priority: AttributePriority
    // TODO: Convert shortName into icon
    val shortName: String
    val displayName: String
    val description: String
    val rangeForRandomizer: AttributeRandomRange

    fun getBaseValue(attrs: AttributesComponent): Float
    fun getDisplayValue(value: Float): String

    fun Float.displayInt() = roundToInt().toString()
    fun Float.displayDecimal(decimals: Int = 2) = "%.${decimals}f".format(this)
    fun Float.displayMultiplier() = "${displayDecimal()}x"
    fun Float.displayPercent() = "${displayDecimal(1)}%"
}

val AttributeType.priority get() = definition.priority
val Attribute.priority get() = type.priority
val AttributeType.shortName get() = definition.shortName
val Attribute.shortName get() = type.shortName
val AttributeType.displayName get() = definition.displayName
val Attribute.displayName get() = type.displayName
val AttributeType.description get() = definition.description
val Attribute.description get() = type.description

fun AttributeType.getBaseValue(attrs: AttributesComponent) = definition.getBaseValue(attrs)
fun Attribute.getBaseValue(attrs: AttributesComponent) = type.getBaseValue(attrs)
fun AttributeType.getDisplayValue(value: Float) = definition.getDisplayValue(value)
fun Attribute.getDisplayValue() = type.getDisplayValue(value)

sealed class PrimaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.PRIMARY
    override val description get() = "$baseDescription Affects ${affects.display()}"
    override val rangeForRandomizer = AttributeRandomRange(5f..10f, 10f..15f)

    override fun getBaseValue(attrs: AttributesComponent) = 100f
    override fun getDisplayValue(value: Float) = value.displayInt()

    protected abstract val baseDescription: String
    protected abstract val affects: Array<AttributeType>
}

sealed class SecondaryAttributeDefinition : AttributeDefinition {
    override val priority = AttributePriority.SECONDARY
    override val description get() = "$baseDescription Affected by ${affectedBy.display()}"

    protected abstract val baseDescription: String
    protected abstract val affectedBy: Array<AttributeType>
}

private fun Array<AttributeType>.display() = joinToString(",") { it.displayName }
