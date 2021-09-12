package com.runt9.fusionOfSouls.model.unit.unitClass

import com.runt9.fusionOfSouls.model.event.OnHitEvent
import com.runt9.fusionOfSouls.model.event.StartTurnEvent
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.model.unit.status.Taunt
import com.soywiz.klock.seconds
import com.soywiz.korio.lang.Closeable
import kotlin.math.roundToInt

class TankClass : UnitClass("Tank") {
    override val synergies: List<ClassSynergy> = listOf(TankLevel1Synergy(), TankLevel2Synergy(), TankLevel3Synergy())
    override val baseAttackRange = 1
}

class TankLevel1Synergy : ClassSynergy(2) {
    private val defenseModifier = AttributeModifier(percentModifier = 0.25)
    private val hpModifier = AttributeModifier(percentModifier = 0.25)

    override fun applyToUnit(unit: GameUnit) {
        unit.secondaryAttrs.defense.addModifier(defenseModifier)
        unit.secondaryAttrs.maxHp.addModifier(hpModifier)
    }

    override fun removeFromUnit(unit: GameUnit) {
        unit.secondaryAttrs.defense.removeModifier(defenseModifier)
        unit.secondaryAttrs.maxHp.removeModifier(hpModifier)
    }
}

class TankLevel2Synergy : ClassSynergy(4) {
//    private var closer: Closeable? = null
    private val hpHealingPercent = 0.05

    override fun applyToUnit(unit: GameUnit) {
//        closer = unit.addEventListener(this::handleStartTurn)
    }

    override fun removeFromUnit(unit: GameUnit) {
//        closer?.close()
    }

    private fun handleStartTurn(event: StartTurnEvent) {
        val unit = event.battleUnit
        val missingHp = unit.unit.secondaryAttrs.maxHp.value - unit.currentHp
        val hpToHeal = (missingHp * hpHealingPercent).roundToInt()
        if (hpToHeal > 0) {
            unit.updateHp(hpToHeal)
        }
    }
}

class TankLevel3Synergy : ClassSynergy(6) {
    private var closer: Closeable? = null
    private val tauntDuration = 1.5.seconds

    override fun applyToUnit(unit: GameUnit) {
        closer = unit.addEventListener(OnHitEvent::class, this::handleOnHit)
    }

    override fun removeFromUnit(unit: GameUnit) {
        closer?.close()
    }

    private fun handleOnHit(event: OnHitEvent) {
        event.defender.addStatusEffect(Taunt(event.attacker), tauntDuration)
    }
}
