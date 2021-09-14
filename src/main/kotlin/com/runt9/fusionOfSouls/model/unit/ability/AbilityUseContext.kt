package com.runt9.fusionOfSouls.model.unit.ability

import com.runt9.fusionOfSouls.service.BattleUnitManager
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.view.BattleUnit

data class AbilityUseContext(val me: BattleUnit, val runState: RunState, val unitManager: BattleUnitManager)
