package com.runt9.fusionOfSouls.model.unit.hero

import com.runt9.fusionOfSouls.model.unit.`class`.TankClass
import com.runt9.fusionOfSouls.model.unit.skill.DefaultSkill
import com.soywiz.korio.file.std.resourcesVfs

val defaultHero = Hero("Default Hero", resourcesVfs["blueArrow-tp.png"], DefaultSkill(), listOf(TankClass()))
