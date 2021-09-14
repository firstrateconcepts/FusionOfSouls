package com.runt9.fusionOfSouls.model.unit.hero

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.unit.ability.DefaultAbility
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass

val defaultHero = Hero("Default Hero", Texture(Gdx.files.internal("blueArrow-tp.png")), DefaultAbility(), listOf(TankClass()))
