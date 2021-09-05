package com.runt9.fusionOfSouls.model.loot.passive

import com.runt9.fusionOfSouls.model.GameUnitEffect
import com.runt9.fusionOfSouls.model.event.BeforeDamageEvent
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.attribute.times
import com.soywiz.korio.lang.Closeable
import kotlin.math.roundToInt

abstract class Passive(val rarity: Rarity) : GameUnitEffect

class DefaultPassive : Passive(Rarity.COMMON) {
    private var listener: Closeable? = null

    override fun applyToUnit(unit: GameUnit) {
        listener = unit.addEventListener(BeforeDamageEvent::class) {
            val extraBaseDamage = it.defender.unit.secondaryAttrs.maxHp * 0.01
            it.damageCalcRequest.additionalBaseDamage += extraBaseDamage.roundToInt()
        }
    }

    override fun removeFromUnit(unit: GameUnit) {
        listener?.close()
    }
}
