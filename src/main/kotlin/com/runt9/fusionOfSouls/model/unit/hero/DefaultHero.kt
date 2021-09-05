package com.runt9.fusionOfSouls.model.unit.hero

import com.runt9.fusionOfSouls.model.unit.skill.DefaultSkill
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass
import com.soywiz.korio.file.std.resourcesVfs

val defaultHero = Hero("Default Hero", resourcesVfs["blueArrow-tp.png"], DefaultSkill(), listOf(TankClass()))
