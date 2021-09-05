package com.runt9.fusionOfSouls.model

import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.view.BattleUnit

interface UnitEffect<T> {
    fun applyToUnit(unit: T)
    fun removeFromUnit(unit: T)
}

interface BattleUnitEffect : UnitEffect<BattleUnit>
interface GameUnitEffect : UnitEffect<GameUnit>
