package com.runt9.fusionOfSouls.model.event

import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.korev.Event

data class StartTurnEvent(val battleUnit: BattleUnit) : Event()
data class EndTurnEvent(val battleUnit: BattleUnit) : Event()
