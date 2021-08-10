package com.runt9.fusionOfSouls.model.event

import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.korev.Event

data class TargetChangedEvent(val me: BattleUnit, val oldTarget: BattleUnit?, val newTarget: BattleUnit) : Event()

data class TargetRemovedEvent(val me: BattleUnit, val oldTarget: BattleUnit) : Event()
