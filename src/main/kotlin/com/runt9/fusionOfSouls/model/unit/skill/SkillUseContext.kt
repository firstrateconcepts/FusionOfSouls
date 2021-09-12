package com.runt9.fusionOfSouls.model.unit.skill

import com.runt9.fusionOfSouls.service.BattleUnitManager
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.view.BattleUnit

data class SkillUseContext(val me: BattleUnit, val runState: RunState, val unitManager: BattleUnitManager)
