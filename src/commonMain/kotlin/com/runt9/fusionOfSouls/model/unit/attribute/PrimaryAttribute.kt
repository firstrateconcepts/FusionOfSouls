package com.runt9.fusionOfSouls.model.unit.attribute

const val baseValue = 100.0

class PrimaryAttribute : Attribute() {
    override fun getBase() = baseValue
}

class PrimaryAttributes {
    val body = PrimaryAttribute()
    val mind = PrimaryAttribute()
    val instinct = PrimaryAttribute()
    val luck = PrimaryAttribute()
    val all = setOf(body, mind, instinct, luck)

    init {
        all.forEach { it.recalculate() }
    }
}
