package com.runt9.fusionOfSouls.model.event

import com.badlogic.gdx.scenes.scene2d.Event
import com.runt9.fusionOfSouls.model.loot.Fusion
import com.runt9.fusionOfSouls.model.unit.hero.Hero

class FusionAddedEvent(val hero: Hero, val fusionAdded: Fusion) : Event() {
    init {
        bubbles = true
        target = hero
    }
}
