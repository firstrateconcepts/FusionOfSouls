package net.firstrateconcepts.fusionofsouls.service.entity

import com.badlogic.gdx.math.Vector2
import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.priority
import net.firstrateconcepts.fusionofsouls.model.component.attrMods
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.view.duringRun.COLS_FOR_UNIT_PLACEMENT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_HEIGHT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_WIDTH
import kotlin.math.ceil
import kotlin.math.max

class EnemyGenerator(
    private val unitManager: UnitManager,
    private val runStateService: RunStateService,
    private val randomizerService: RandomizerService
) {
    private fun enemyCount(runState: RunState) = ceil(max((((runState.floor - 1) * 10.0) + runState.room) / 3.0, 1.0)).toInt()
    private fun enemyStrength(runState: RunState) = -25.0 + (((((runState.floor - 1) * 10.0) + runState.room) - 1) * 2.5)

    fun generateEnemies() {
        val state = runStateService.load()
        val count = enemyCount(state)
        val strength = enemyStrength(state)
        val availablePositions = randomizerService.randomize {
            ((GAME_WIDTH - COLS_FOR_UNIT_PLACEMENT - 1).toInt() until (GAME_WIDTH - 1).toInt()).flatMap { x ->
                (0 until (GAME_HEIGHT - 1).toInt()).map { y -> Vector2(x.toFloat(), y.toFloat()) }
            }.shuffled().toMutableList()
        }

        (0 until count).forEach {
            val position = availablePositions.removeFirst()
            val unit = unitManager.buildUnit("Enemy $it", UnitTexture.ENEMY, UnitType.BASIC, UnitTeam.ENEMY) {
                entity.attrMods.apply {
                    // TODO: Randomize enemy stats more than just the strength mods to their base attrs
                    AttributeType.values().filter { v -> v.priority == AttributePriority.PRIMARY }.forEach { type ->
                        add(AttributeModifier(type, percentModifier = strength.toFloat()))
                    }
                }
            }

            unitManager.activateUnit(unit.id, position)
        }
    }
}
