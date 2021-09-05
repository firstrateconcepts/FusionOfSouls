package com.runt9.fusionOfSouls.util

import com.badlogic.gdx.scenes.scene2d.Stage

fun Stage.render(delta: Float) {
    this.run {
        viewport.apply()
        act(delta)
        isDebugAll = true

        draw()
    }
}
