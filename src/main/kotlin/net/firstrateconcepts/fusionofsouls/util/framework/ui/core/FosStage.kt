package net.firstrateconcepts.fusionofsouls.util.framework.ui.core

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.View

class FosStage(viewport: Viewport = ScreenViewport()) : Stage(viewport) {
    fun render(delta: Float) {
        viewport.apply()
        act(delta)
        draw()
    }

    fun setView(view: View) {
        (view as Group).run {
            root = view
            view.init()
        }
    }
}
