package com.runt9.fusionOfSouls.model.unit.attribute

val randomMaxHp = AttributeModifierRandomizer(15, 25, 10..20)
val randomBaseDamage = AttributeModifierRandomizer(3, 7)
val randomSkillMulti = AttributeModifierRandomizer(0.1, 0.15, 5..10)
val randomDefense = AttributeModifierRandomizer(1, 2)
val randomEvasion = AttributeModifierRandomizer(1, 2, 5..10)
val randomCritThreshold = AttributeModifierRandomizer(-2, -1, -5..-3)
val randomCritMulti = AttributeModifierRandomizer(0.1, 0.15, 5..10)
val randomAttackSpeed = AttributeModifierRandomizer(0.025, 0.05)
val randomCdr = AttributeModifierRandomizer(0.05, 0.1, 2..4)
