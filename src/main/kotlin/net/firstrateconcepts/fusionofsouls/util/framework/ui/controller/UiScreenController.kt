package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Group
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.config.lazyInject
import net.firstrateconcepts.fusionofsouls.util.ext.FosScreen
import net.firstrateconcepts.fusionofsouls.util.ext.FosStage

abstract class UiScreenController : Controller(), FosScreen {
    val uiStage = FosStage()
    override val stages = listOf(uiStage)
    override val game by lazyInject<FusionOfSoulsGame>()
    protected val input by lazyInject<InputMultiplexer>()

    override fun show() {
        input.addProcessor(uiStage)
        uiStage.root = view as Group
        view.init()
    }

    override fun hide() {
        input.removeProcessor(uiStage)
        super<Controller>.dispose()
    }

    override fun dispose() {
        super<FosScreen>.dispose()
    }

    inline fun <reified D : DialogController> showDialog() {
        val dialog = inject<D>()
        dialog.show(uiStage)
    }
}
