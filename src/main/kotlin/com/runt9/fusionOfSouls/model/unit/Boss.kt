package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.loot.Fusible
import com.runt9.fusionOfSouls.model.loot.Fusion
import com.runt9.fusionOfSouls.model.unit.ability.DefaultAbility
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass
import ktx.scene2d.vis.KVisTable

class Boss : GameUnit("Boss", Texture(Gdx.files.internal("bossArrow-tp.png")), DefaultAbility(), listOf(TankClass())) {
    val fusions = mutableListOf<Fusion>()

    override fun KVisTable.additionalTooltipData() {

    }

    init {
        savedGridPos = GridPoint(12, 4)
        primaryAttrs.all.forEach { it.addModifier(percentModifier = 25.0) }
    }

    fun addFusion(selectedFusion: Fusion) {
        selectedFusion.applyToUnit(this)
        fusions += selectedFusion
    }

    fun addFusible(fusible: Fusible) {
        addFusion(Fusion(fusible.getFusibleEffects().random()))
    }
}
