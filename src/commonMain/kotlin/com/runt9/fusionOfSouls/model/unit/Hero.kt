package com.runt9.fusionOfSouls.model.unit

import com.runt9.fusionOfSouls.model.GridPoint
import com.soywiz.korim.bitmap.Bitmap

class Hero(name: String, unitImage: Bitmap) : GameUnit(name, unitImage) {
    init {
        // Hero cannot be removed so must start on the grid
        savedGridPos = GridPoint(0.0, 0.0)
    }
}
