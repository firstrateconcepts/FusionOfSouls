package com.runt9.fusionOfSouls.model.loot

import com.badlogic.gdx.scenes.scene2d.EventListener
import com.runt9.fusionOfSouls.view.duringRun.fuseMenuItem
import ktx.scene2d.scene2d
import ktx.scene2d.vis.popupMenu

interface Fusible {
    fun getFusibleEffects(): List<FusibleEffect>
    fun onFusionChosen(fusion: Fusion)
    fun addListener(listener: EventListener): Boolean

    fun addRightClickMenu() {
        val menu = scene2d.popupMenu {
            fuseMenuItem(this@Fusible)
        }

        addListener(menu.defaultInputListener)
    }
}
