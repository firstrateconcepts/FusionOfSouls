package com.runt9.fusionOfSouls.view

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.Team
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.View
import com.soywiz.korge.view.center
import com.soywiz.korge.view.image
import com.soywiz.korge.view.rotation
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.Bitmap

enum class BattleUnitState {
    MOVING, ATTACKING, USING_SKILL
}

class BattleUnit(val unit: GameUnit, val team: Team, var gridPos: GridPoint, unitImage: Bitmap) : Container() {
    val body: View
    val currentHp = unit.secondaryAttrs.maxHp
    val states = mutableListOf<BattleUnitState>()
    var aggroRangeFlat = 2
    var previousGridPos: GridPoint? = null
    var movingToGridPos: GridPoint? = null
    var currentTarget: BattleUnit? = null


    init {
        name = unit.name

        xy(gridPos.worldX, gridPos.worldY)
        body = image(unitImage).center().rotation(team.initialRotation)
    }
}
