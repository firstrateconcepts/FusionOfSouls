package net.firstrateconcepts.fusionofsouls.model.attribute

import net.firstrateconcepts.fusionofsouls.model.attribute.definition.displayName
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getDisplayValue
import net.firstrateconcepts.fusionofsouls.util.ext.FloatRange

data class Attribute(val type: AttributeType) {
    var value = 0f

    operator fun invoke() = value
    operator fun invoke(value: Float) {
        this.value = value
    }

    override fun toString() = "${displayName}: ${getDisplayValue()}"
}

class AttributeRandomRange(val flat: FloatRange, val percent: FloatRange)
class AttributeValueClamp(val min: Float? = null, val max: Float? = null)
