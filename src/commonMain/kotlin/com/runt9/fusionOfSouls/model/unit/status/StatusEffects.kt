package com.runt9.fusionOfSouls.model.unit.status

import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.klock.seconds
import com.soywiz.korge.view.addFixedUpdater

class StatusEffects(val unit: BattleUnit) {
    fun addEffect() {
        unit.addFixedUpdater(1.5.seconds, initial = false) {

        }
    }
}
