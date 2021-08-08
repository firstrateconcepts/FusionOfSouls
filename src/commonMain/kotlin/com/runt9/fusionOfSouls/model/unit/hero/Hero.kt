package com.runt9.fusionOfSouls.model.unit.hero

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.skill.Skill
import com.soywiz.korio.file.VfsFile

class Hero(name: String, unitImage: VfsFile, skill: Skill) : GameUnit(name, unitImage, skill) {
    init {
        // Hero cannot be removed so must start on the grid
        savedGridPos = GridPoint(0.0, 0.0)
    }
}
