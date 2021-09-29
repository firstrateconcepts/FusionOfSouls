package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

class FosStage(viewport: Viewport = ScreenViewport()) : Stage(viewport) {
    private val controllers = mutableListOf<Controller>()

    fun render(delta: Float) {
        controllers.forEach { it.render(delta) }
        viewport.apply()
        act(delta)
        draw()
    }

    fun attachController(controller: Controller, root: Boolean = true) {
        controllers.add(controller)
        if (root) {
            this.root = controller.view as Group
        }
        controller.view.init()
        controller.addedToStage(this)
    }

    fun detachController(controller: Controller) {
        controller.run {
            controllers.remove(this)
            removedFromStage()
            dispose()
        }
    }

    fun clearControllers() = controllers.toList().forEach(this::detachController)
}
