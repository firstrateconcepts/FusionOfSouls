package com.runt9.fusionOfSouls.model.unit

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import com.soywiz.korim.bitmap.Bitmap

open class GameUnit(val name: String, val unitImage: Bitmap) {
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)

    // Null means on bench
    var savedGridPos: GridPoint? = null

    fun purgeTemporaryModifiers() {
        secondaryAttrs.purgeTemporaryModifiers()
        primaryAttrs.purgeTemporaryModifiers()
    }
}
