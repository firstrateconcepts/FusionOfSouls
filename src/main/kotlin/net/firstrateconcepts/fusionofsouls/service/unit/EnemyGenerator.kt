package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.gdx.math.Vector2
import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeModifier
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.priority
import net.firstrateconcepts.fusionofsouls.model.component.attrMods
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.loot.Rarity
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.ability.GroundSlam
import net.firstrateconcepts.fusionofsouls.model.unit.unitClass.Fighter
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.util.ext.matchOrdinal
import net.firstrateconcepts.fusionofsouls.util.ext.sqrt
import net.firstrateconcepts.fusionofsouls.view.duringRun.COLS_FOR_UNIT_PLACEMENT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_HEIGHT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_WIDTH
import kotlin.math.roundToInt

class EnemyGenerator(
    private val unitManager: UnitManager,
    private val runStateService: RunStateService,
    private val randomizerService: RandomizerService,
    private val passiveService: PassiveService
) {
    private val RunState.floorRoomTotal get() = floor.minus(1).times(10).plus(room)
    // See test for expected output for these two functions
    fun enemyCount(runState: RunState) = runState.floorRoomTotal.times(1.5).sqrt().roundToInt()
    fun enemyStrength(runState: RunState) = runState.floorRoomTotal.times(runState.floorRoomTotal.sqrt()).minus(26).roundToInt()
    private val enemyPositionPossibilities = ((GAME_WIDTH - COLS_FOR_UNIT_PLACEMENT - 1).toInt()..(GAME_WIDTH - 1).toInt()).flatMap { x ->
        (0..(GAME_HEIGHT - 1).toInt()).map { y -> Vector2(x.toFloat(), y.toFloat()) }
    }

    fun generateEnemies() {
        val state = runStateService.load()
        val count = enemyCount(state)
        val strength = enemyStrength(state)
        val rarity = (state.floor - 1).matchOrdinal<Rarity>()!!
        val availablePositions = randomizerService.randomize { enemyPositionPossibilities.shuffled(it).toMutableList() }

        (0 until count).forEach {
            val position = availablePositions.removeFirst()
            // TODO: Ability pool random etc
            val ability = GroundSlam
            val passive = passiveService.randomPassive(rarity)
            val unitClass = Fighter
            val entity = unitManager.buildUnit("Enemy $it", UnitTexture.ENEMY, ability, passive, UnitType.BASIC, UnitTeam.ENEMY, listOf(unitClass)) {
                // TODO: Randomize enemy stats more than just the strength mods to their base attrs
                AttributeType.values().filter { v -> v.priority == AttributePriority.PRIMARY }.forEach { type ->
                    entity.attrMods.add(AttributeModifier(type, percentModifier = strength.toFloat()))
                }
            }
            unitManager.activateUnit(entity.id, position)
        }
    }
}
