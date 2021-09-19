package com.runt9.fusionOfSouls.view.duringRun.charDialog.tab

import com.runt9.fusionOfSouls.service.runState
import com.soywiz.korma.math.roundDecimalPlaces
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visScrollPane

class AbilityTab : CharDialogTab("Ability") {
    init {
        (contentTable as KVisTable).apply {
            val ability = runState.hero.ability
            visLabel(ability.name).cell(growX = true, row = true)
            val cooldownText = { "Cooldown: ${ability.modifiedCooldown.roundDecimalPlaces(1)}s (${ability.baseCooldown}s)" }
            visLabel(cooldownText(), style = "small") {
                setFontScale(0.6f)
                runState.hero.secondaryAttrs.cooldownReduction.addListener {
                    setText(cooldownText())
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
