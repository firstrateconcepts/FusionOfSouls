package net.firstrateconcepts.fusionofsouls.util.framework.ui.core

import com.badlogic.gdx.InputMultiplexer
import net.firstrateconcepts.fusionofsouls.config.lazyInject
import net.firstrateconcepts.fusionofsouls.util.framework.ui.DialogManager
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

abstract class UiScreen : FosScreen {
    val uiStage = FosStage()
    val input by lazyInject<InputMultiplexer>()
    val dialogManager by lazyInject<DialogManager>()
    override val stages = listOf(uiStage)
    abstract val uiController: Controller

    override fun show() {
        input.addProcessor(uiStage)
        uiController.load()
        uiStage.setView(uiController.view)
        dialogManager.currentStage = uiStage
    }

    override fun hide() {
        input.removeProcessor(uiStage)
        uiController.dispose()
        dialogManager.currentStage = null
    }
}
