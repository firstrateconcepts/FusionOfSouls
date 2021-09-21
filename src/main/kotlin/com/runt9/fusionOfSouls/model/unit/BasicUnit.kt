package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import com.runt9.fusionOfSouls.model.loot.Passive
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.runt9.fusionOfSouls.util.scaledLabel
import com.runt9.fusionOfSouls.view.duringRun.fuseMenuItem
import ktx.scene2d.scene2d
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.popupMenu

class BasicUnit(
    name: String,
    unitImage: Texture,
    ability: Ability,
    val team: Team,
    val passive: Passive,
    val attributeModifier: AttributeModifierEffect<*, *>,
    classes: List<UnitClass>
) : GameUnit(name, unitImage, ability, classes) {
    init {
        passive.applyToUnit(this)
        attributeModifier.applyToUnit(this)

        if (team == Team.PLAYER) {
            addRightClickMenu()
        }
    }

    private fun addRightClickMenu() {
        // TODO: Make menu items smaller. They're buttons, just need styling
        val menu = scene2d.popupMenu {
//            menuItem("More Info")
            fuseMenuItem()
        }

        addListener(menu.defaultInputListener)
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
