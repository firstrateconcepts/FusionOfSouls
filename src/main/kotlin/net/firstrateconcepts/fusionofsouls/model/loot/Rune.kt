package net.firstrateconcepts.fusionofsouls.model.loot

import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition

class Rune(val id: Int, val name: String, val modifiers: List<AttributeModifier>, val passive: PassiveDefinition? = null) {
    var active = false
    val description by lazy { generateDescription() }

    private fun generateDescription(): String {
        val sb = StringBuilder()
        modifiers.forEach { sb.append("${it.name}\n") }
        passive?.let { sb.append("\n${it.name}") }
        return sb.toString()
    }
}
