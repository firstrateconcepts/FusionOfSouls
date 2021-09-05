package com.runt9.fusionOfSouls.model.unit.hero

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.unit.skill.DefaultSkill
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass

val defaultHero = Hero("Default Hero", Texture(Gdx.files.internal("blueArrow-tp.png")), DefaultSkill(), listOf(TankClass()))
