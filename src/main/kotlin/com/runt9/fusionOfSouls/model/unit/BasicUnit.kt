package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass

class BasicUnit(
    name: String,
    unitImage: Texture,
    ability: Ability,
    val passive: Passive,
    val attributeModifier: AttributeModifierEffect<*, *>,
    classes: List<UnitClass>
) : GameUnit(name, unitImage, ability, classes) {
    init {
        passive.applyToUnit(this)
        attributeModifier.applyToUnit(this)
    }
}
