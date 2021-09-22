package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.model.loot.Fusible
import com.runt9.fusionOfSouls.model.loot.Fusion
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.scaledLabel
import ktx.scene2d.vis.KVisTable

class BasicUnit(
    name: String,
    unitImage: Texture,
    ability: Ability,
    val team: Team,
    val passive: Passive,
    val attributeModifier: AttributeModifierEffect<*, *>,
    classes: List<UnitClass>
) : GameUnit(name, unitImage, ability, classes), Fusible {
    var isActive = false

    init {
        passive.applyToUnit(this)
        attributeModifier.applyToUnit(this)

        if (team == Team.PLAYER) {
            addRightClickMenu()
        }
    }

    override fun KVisTable.additionalTooltipData() {
        row()
        scaledLabel(attributeModifier.description).cell(row = true, colspan = 2)
        scaledLabel(passive.description) {
            wrap = true
            setAlignment(Align.center)
        }.cell(colspan = 2, growX = true)
    }

    override fun getFusibleEffects() = listOf(passive, attributeModifier)

    override fun onFusionChosen(fusion: Fusion) {
        runState.hero.fuseUnit(this, fusion)
    }

}
