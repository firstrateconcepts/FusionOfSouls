package com.runt9.fusionOfSouls.model.loot

enum class Rarity {
    COMMON, UNCOMMON, RARE, LEGENDARY;

    companion object {
        fun getFromFloor(floor: Int) = Rarity.values().find { it.ordinal == (floor - 1) }!!
    }
}
