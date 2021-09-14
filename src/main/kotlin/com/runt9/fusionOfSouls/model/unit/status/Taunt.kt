package com.runt9.fusionOfSouls.model.unit.status

import com.runt9.fusionOfSouls.model.event.TargetRemovedEvent
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.korev.addEventListener
import com.soywiz.korio.lang.Closeable

// TODO: This can be unit tested
class Taunt(private val tauntTarget: BattleUnit) : StatusEffect("Taunt") {
    private val defenseReductionValue = -25.0
    override val description = "Taunted unit must target the unit that taunted it and has ${defenseReductionValue}% reduced Defense for the duration of the Taunt."
    private val defenseModifier = AttributeModifier(percentModifier = defenseReductionValue, isTemporary = true)
    private var originalTarget: BattleUnit? = null
    private lateinit var targetRemovedCloser: Closeable

    override fun applyToUnit(unit: BattleUnit) {
        unit.unit.secondaryAttrs.defense.addModifier(defenseModifier)

        originalTarget = unit.target
        unit.changeTarget(tauntTarget)
        unit.canChangeTarget = false
        targetRemovedCloser = unit.addEventListener<TargetRemovedEvent> { unit.removeStatusEffect(this@Taunt) }
    }

    override fun removeFromUnit(unit: BattleUnit) {
        unit.unit.secondaryAttrs.defense.removeModifier(defenseModifier)

        unit.canChangeTarget = true
        originalTarget?.let {
            if (it.isAlive) {
                unit.changeTarget(it)
            }
        }

        targetRemovedCloser.close()
    }
}
