package com.runt9.fusionOfSouls.view.duringRun.charDialog.tab

import com.runt9.fusionOfSouls.service.runState
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visScrollPane

class AbilityTab : CharDialogTab("Ability") {
    init {
        (contentTable as KVisTable).apply {
            val ability = runState.hero.ability
            val abilityText = { ability.displayStr }

            visLabel(abilityText()) {
                setFontScale(0.6f)
                runState.hero.secondaryAttrs.cooldownReduction.addListener {
                    setText(abilityText())
                }
            }.cell(growX = true, row = true)

            visScrollPane {
                setScrollingDisabled(true, false)
                visLabel(ability.description, style = "small") {
                    setFontScale(0.75f)
                    wrap = true
                }
            }.cell(grow = true)
        }
    }
}
