package com.runt9.fusionOfSouls.model.unit.attribute

interface Attributes<T : Attribute> {
    val all: List<T>

    fun purgeTemporaryModifiers() {
        all.forEach(Attribute::purgeTemporaryModifiers)
    }
}
