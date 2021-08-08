package com.runt9.fusionOfSouls.model.unit

import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import com.soywiz.korim.bitmap.Bitmap
import kotlin.random.Random

open class GameUnit(val name: String, val unitImage: Bitmap) {
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)
    val attackRange: Int

    // Null means on bench
    var savedGridPos: GridPoint? = null

    init {
        // TODO: Attack range based off of incoming class(s)
        attackRange = Random.nextInt(1, 5)
    }

    fun purgeTemporaryModifiers() {
        secondaryAttrs.purgeTemporaryModifiers()
        primaryAttrs.purgeTemporaryModifiers()
    }
}
