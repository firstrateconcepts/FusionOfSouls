package net.firstrateconcepts.fusionofsouls.model.attribute

import net.firstrateconcepts.fusionofsouls.model.attribute.definition.displayName
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getDisplayValue
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

data class AttributeModifier(val type: AttributeType, val flatModifier: Float = 0f, val percentModifier: Float = 0f, val isTemporary: Boolean = false) {
    val name by lazy { generateDisplayName() }

    private fun generateDisplayName(): String {
        val sb = StringBuilder()

        val hasFlat = flatModifier != 0f
        if (hasFlat) {
            if (flatModifier > 0f) sb.append("+")
            sb.append("${type.getDisplayValue(flatModifier)} ${type.displayName}")
        }
        if (percentModifier != 0f) {
            if (hasFlat) sb.append("\n")
            val incRed = if (percentModifier > 0.0) "increased" else "reduced"
            sb.append("${percentModifier.roundToInt().absoluteValue}% $incRed ${type.displayName}")
        }

        return sb.toString()
    }
}
