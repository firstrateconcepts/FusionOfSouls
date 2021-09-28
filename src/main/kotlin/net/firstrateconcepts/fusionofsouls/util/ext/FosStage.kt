package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.scenes.scene2d.Stage

class FosStage : Stage() {
    fun render(delta: Float) {
        run {
            viewport.apply()
            act(delta)
            draw()
        }
    }
}
