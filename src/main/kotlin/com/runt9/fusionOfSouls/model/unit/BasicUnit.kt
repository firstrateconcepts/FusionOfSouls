package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.runt9.fusionOfSouls.util.scaledLabel
import ktx.scene2d.vis.KVisTable

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

    override fun KVisTable.additionalTooltipData() {
        row()
        scaledLabel(attributeModifier.description).cell(row = true, colspan = 2)
        scaledLabel(passive.description) {
            wrap = true
            setAlignment(Align.center)
        }.cell(colspan = 2, growX = true)
    }
}
