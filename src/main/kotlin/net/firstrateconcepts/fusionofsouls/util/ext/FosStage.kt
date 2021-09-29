package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Controller

class FosStage(viewport: Viewport = ScreenViewport()) : Stage(viewport) {
    private var controller: Controller? = null

    fun render(delta: Float) {
        controller?.render(delta)
        viewport.apply()
        act(delta)
        draw()
    }

    fun attachController(controller: Controller) {
        this.controller = controller
        root = controller.view
        controller.view.init()
        controller.addedToStage()
    }

    fun detachController() {
        controller?.run {
            removedFromStage()
            dispose()
        }

        controller = null
    }
}
