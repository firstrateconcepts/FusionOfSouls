package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport

class FosStage(viewport: Viewport = ScreenViewport()) : Stage(viewport) {
    fun render(delta: Float) {
        viewport.apply()
        act(delta)
        draw()
    }
}
