package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.loot.Rune
import com.runt9.fusionOfSouls.model.unit.BasicUnit
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
    val inactiveUnitAddedListeners = mutableListOf<(BasicUnit) -> Unit>()
    val roomNumberListeners = mutableListOf<(Int) -> Unit>()
    val statusListeners = mutableListOf<(BattleStatus) -> Unit>()

    var floor = 1
    var room by simpleObservable(1, roomNumberListeners)

    lateinit var hero: Hero
    var activeUnits by simpleObservable(listOf(), activeUnitListeners)
    val inactiveUnits = mutableListOf<BasicUnit>() //by simpleObservable(listOf(), inactiveUnitAddedListeners)
    val unequippedRunes = mutableListOf<Rune>()
    var gold by simpleObservable(0, goldListeners)

    lateinit var battleContext: BattleContext
    var battleStatus by simpleObservable(BattleStatus.BEFORE, statusListeners)

    // TODO: Not hard-coded
    var unitCap = 2
    var runeCap = 1
    var fusionCap = 3
    var rng = Random(Random.nextBytes(32).hashCode())

    fun overrideSeed(seed: Any) {
        this.rng = Random(seed.hashCode())
    }

    fun addNewUnit(unit: BasicUnit) {
        inactiveUnits += unit
        inactiveUnitAddedListeners.forEach { it(unit) }
    }
}
