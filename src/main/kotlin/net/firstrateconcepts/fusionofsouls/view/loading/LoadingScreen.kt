package net.firstrateconcepts.fusionofsouls.view.loading

import net.firstrateconcepts.fusionofsouls.util.ext.UiScreen
import net.firstrateconcepts.fusionofsouls.util.ext.changeScreen
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen

class LoadingScreen(private val controller: LoadingScreenController) : UiScreen() {
    private val logger = fosLogger()

    override fun show() {
        uiStage.addActor(controller.view.viewDefinition)
        controller.addedToStage()
        controller.onLoadingComplete = {
            logger.debug { "Loading complete, moving to main menu" }
            changeScreen<MainMenuScreen>()
            destroy()
        }
    }

    override fun render(delta: Float) {
        controller.render(delta)
        super.render(delta)
    }

    override fun hide() {
        super.hide()
        controller.removedFromStage()
    }
}
