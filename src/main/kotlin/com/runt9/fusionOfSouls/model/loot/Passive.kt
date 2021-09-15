package com.runt9.fusionOfSouls.model.loot

import com.runt9.fusionOfSouls.model.event.BeforeDamageEvent
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.attribute.times
import com.soywiz.korio.lang.Closeable
import kotlin.math.roundToInt

abstract class Passive(val rarity: Rarity) : FusableEffect {
    override val fusionType = FusionType.PASSIVE
}

class DefaultPassive : Passive(Rarity.COMMON) {
    private val value = 0.01
    override val description = "Attacks and abilities deal additional damage equal to ${value * 100}% of the target's Maximum HP."
    private var listener: Closeable? = null

    override fun applyToUnit(unit: GameUnit) {
        listener = unit.addEventListener(BeforeDamageEvent::class) {
            val extraBaseDamage = it.defender.unit.secondaryAttrs.maxHp * value
            it.damageCalcRequest.additionalBaseDamage += extraBaseDamage.roundToInt()
        }
    }

    override fun removeFromUnit(unit: GameUnit) {
        listener?.close()
    }
}

class TestPassive : Passive(Rarity.COMMON) {
    override val description = "Gain 50% increased attack speed for 4 seconds after killing an enemy unit"

    override fun applyToUnit(unit: GameUnit) {
    }

    override fun removeFromUnit(unit: GameUnit) {
    }

}
