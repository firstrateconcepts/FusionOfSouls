package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog

import com.badlogic.gdx.graphics.Texture
import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.loot.AbilityAugment
import net.firstrateconcepts.fusionofsouls.model.loot.Rune
import net.firstrateconcepts.fusionofsouls.model.loot.Synergy
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.unitClass.UnitClass
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class UnitDialogViewModel(val id: Int, val name: String, val texture: Texture, val passive: PassiveDefinition) : ViewModel() {
    val attrs = Binding<Collection<Attribute>>(listOf())

    val abilityName = Binding("")
    val abilityDescription = Binding("")
    val abilityBaseCooldown = Binding(0f)
    val abilityCurrentCooldown = Binding(0f)

    val runes = Binding(listOf<Rune>())
    val runeCap = Binding(0)

    val xp = Binding(0)
    val xpToLevel = Binding(0)
    val level = Binding(0)

    val classes = Binding(mapOf<UnitClass, Int>())

    val passives = Binding(listOf<PassiveDefinition>())
    val attrMods = Binding(listOf<AttributeModifier>())
    val abilityAugs = Binding(listOf<AbilityAugment>())
    val synergies = Binding(listOf<Synergy>())
    val fusionCount = Binding(0)
    val fusionCap = Binding(0)

    // TODO: This has to be a binding from a binding????
    val activeRuneCount get() = runes.get().count { it.active }
}
