package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.loot.Rune
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.Boss
import com.runt9.fusionOfSouls.model.unit.Team
import com.runt9.fusionOfSouls.model.unit.hero.Hero
import com.runt9.fusionOfSouls.util.simpleObservable
import com.runt9.fusionOfSouls.view.BattleUnit
import kotlin.random.Random

data class BattleContext(val enemies: List<BattleUnit>, val hero: BattleUnit) {
    val enemyCount = enemies.size
    var flawless: Boolean = true
    var heroLived: Boolean = true
    val playerTeam = mutableListOf<BattleUnit>()
    val enemyTeam = mutableListOf<BattleUnit>()
    val allUnits
        get() = playerTeam + enemyTeam

    fun clear() {
        playerTeam.forEach(BattleUnit::remove)
        enemyTeam.forEach(BattleUnit::remove)
    }
}

enum class BattleStatus {
    BEFORE, DURING, AFTER
}

class RunState {
    val goldListeners = mutableListOf<(Int) -> Unit>()
    val activeUnitListeners = mutableListOf<(List<BasicUnit>) -> Unit>()
    val activeUnitAddedListeners = mutableListOf<(BasicUnit) -> Unit>()
    val activeUnitRemovedListeners = mutableListOf<(BasicUnit) -> Unit>()
    val inactiveUnitAddedListeners = mutableListOf<(BasicUnit) -> Unit>()
    val roomNumberListeners = mutableListOf<(Int) -> Unit>()
    val statusListeners = mutableListOf<(BattleStatus) -> Unit>()

    var floor = 1
    var room by simpleObservable(1, roomNumberListeners)

    lateinit var hero: Hero
    lateinit var boss: Boss
    var activeUnits = mutableListOf<BasicUnit>()
    val inactiveUnits = mutableListOf<BasicUnit>()
    val unequippedRunes = mutableListOf<Rune>()
    var gold by simpleObservable(0, goldListeners)

    lateinit var battleContext: BattleContext
    var battleStatus by simpleObservable(BattleStatus.BEFORE, statusListeners)

    // TODO: Not hard-coded
    var unitCap = 3
    var runeCap = 2
    var fusionCap = 5
    var rng = Random(Random.nextBytes(32).hashCode())

    fun overrideSeed(seed: Any) {
        this.rng = Random(seed.hashCode())
    }

    fun addNewUnit(unit: BasicUnit) {
        inactiveUnits += unit
        inactiveUnitAddedListeners.forEach { it(unit) }
    }

    fun activateUnit(unit: BasicUnit): BattleUnit {
        inactiveUnits -= unit
        activeUnits += unit
        unit.isActive = true

        // NB: BattleUnit must be initialized before calling listeners in case they need the newly
        // created BattleUnit link in the GameUnit in order to do any work
        val battleUnit = BattleUnit(unit, Team.PLAYER)

        activeUnitAddedListeners.forEach { it(unit) }
        activeUnitListeners.forEach { it(activeUnits) }

        return battleUnit
    }

    fun deactivateUnit(unit: BasicUnit) {
        inactiveUnits += unit
        activeUnits -= unit
        unit.isActive = false

        activeUnitRemovedListeners.forEach { it(unit) }
        activeUnitListeners.forEach { it(activeUnits) }

        // Called after listeners so listeners may use any prior state for logic before the unit is reset
        unit.reset()
    }

    fun removeUnit(unit: BasicUnit) {
        if (unit.isActive) {
            activeUnits -= unit
            activeUnitRemovedListeners.forEach { it(unit) }
            if (unit.parent is BattleUnit) {
                unit.parent.remove()
            }
        } else {
            inactiveUnits -= unit
        }

        unit.remove()
        activeUnitListeners.forEach { it(activeUnits) }
    }
}
