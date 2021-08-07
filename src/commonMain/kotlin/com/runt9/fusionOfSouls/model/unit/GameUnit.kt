package com.runt9.fusionOfSouls.model.unit

import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes

class GameUnit(val name: String) {
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)
}
