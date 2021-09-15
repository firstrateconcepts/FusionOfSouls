package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.loot.Rune
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.hero.Hero
import kotlin.properties.Delegates
import kotlin.random.Random

class RunState {
    val goldListeners = mutableListOf<(Int) -> Unit>()
    val unitListeners = mutableListOf<() -> Unit>()

    var floor = 1
    var room = 1

    lateinit var hero: Hero
    val activeUnits = mutableListOf<BasicUnit>()
    val inactiveUnits = mutableListOf<BasicUnit>()
    val unequippedRunes = mutableListOf<Rune>()
    var gold by Delegates.observable(0) { _, _, newValue -> goldListeners.forEach { it(newValue) } }

    lateinit var battleContext: BattleContext

    // TODO: Not hard-coded
    var unitCap = 2
    var runeCap = 1
    var fusionCap = 3
    var rng = Random(Random.nextBytes(32).hashCode())

    fun overrideSeed(seed: Any) {
        this.rng = Random(seed.hashCode())
    }

    fun addUnit(unit: BasicUnit) {
        inactiveUnits.add(unit)
        unitListeners.forEach { it() }
    }
}
