package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.loot.rune.Rune
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.hero.Hero
import kotlin.random.Random

class RunState {
    var floor = 1
    var room = 1

    lateinit var hero: Hero
    val activeUnits = mutableListOf<GameUnit>()
    val inactiveUnits = mutableListOf<GameUnit>()
    val unequippedRunes = mutableListOf<Rune>()
    var gold = 0

    // TODO: Not hard-coded
    var unitCap = 2
    var runeCap = 1
    var fusionCap = 3
    var rng = Random(Random.nextBytes(32).hashCode())

    fun overrideSeed(seed: Any) {
        this.rng = Random(seed.hashCode())
    }
}
