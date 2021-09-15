package com.runt9.fusionOfSouls.service

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.gridWidth
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.view.BattleUnit

class EnemyGenerator(private val gridService: GridService, private val unitGenerator: UnitGenerator) {
    fun generateEnemies(count: Int, strength: Double): List<BattleUnit> {
        return (0 until count).map { generateEnemy(strength) }
    }

    fun generateEnemy(strength: Double): BattleUnit {
        val randomEnemyPoint = gridService.addRandomlyToGrid(gridWidth - 5, gridWidth - 1)
        val enemyUnit = unitGenerator.generateUnit(Rarity.getFromFloor(runState.floor), Texture(Gdx.files.internal("redArrow-tp.png")))
        enemyUnit.savedGridPos = randomEnemyPoint
        val enemyUnitView = BattleUnit(enemyUnit, Team.ENEMY)
        enemyUnit.primaryAttrs.all.forEach {
            it.addModifier(AttributeModifier(percentModifier = strength))
        }
        return enemyUnitView
    }
}
