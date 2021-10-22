package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.loot.AbilityAugment
import net.firstrateconcepts.fusionofsouls.model.loot.Synergy
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition

class FusionsComponent : Component {
    val passives = mutableListOf<PassiveDefinition>()
    val attrMods = mutableListOf<AttributeModifier>()
    val abilityAugs = mutableListOf<AbilityAugment>()
    val synergies = mutableListOf<Synergy>()

    val fusionCount = passives.size + attrMods.size + abilityAugs.size + synergies.size
}
val fusionsMapper = mapperFor<FusionsComponent>()
val Entity.fusions get() = this[fusionsMapper]!!
