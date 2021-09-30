package net.firstrateconcepts.fusionofsouls.util.framework.ui.core

import com.badlogic.gdx.InputMultiplexer
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.config.lazyInject
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController

abstract class UiScreen : FosScreen {
    val uiStage = FosStage()
    val input by lazyInject<InputMultiplexer>()
    override val stages = listOf(uiStage)
    abstract val uiController: Controller

    override fun show() {
        input.addProcessor(uiStage)
        uiStage.setView(uiController.view)
    }

    override fun hide() {
        input.removeProcessor(uiStage)
        uiController.dispose()
    }

    inline fun <reified D : DialogController> showDialog() {
        val dialog = inject<D>()
        dialog.show(uiStage)
    }
}
