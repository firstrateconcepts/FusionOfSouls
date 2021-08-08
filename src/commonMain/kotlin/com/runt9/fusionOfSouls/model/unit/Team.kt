package com.runt9.fusionOfSouls.model.unit

import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.degrees

enum class Team(val initialRotation: Angle) {
    PLAYER(0.degrees), ENEMY(180.degrees)
}
