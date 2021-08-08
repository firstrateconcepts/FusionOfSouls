package com.runt9.fusionOfSouls.service

import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.hero.Hero

class RunState {
    var floor = 1
    var room = 1

    lateinit var hero: Hero
    val units = mutableListOf<GameUnit>()
    var gold = 0

    // TODO: Not hard-coded
    var unitCap = 2
    var itemCap = 1
    var fusionCap = 3
}
