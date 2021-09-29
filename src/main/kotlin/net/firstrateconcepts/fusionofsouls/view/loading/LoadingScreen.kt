package net.firstrateconcepts.fusionofsouls.view.loading

import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.event.AssetsLoadedEvent
import net.firstrateconcepts.fusionofsouls.util.ext.UiScreen
import net.firstrateconcepts.fusionofsouls.util.ext.changeScreen
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventHandler
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen

class LoadingScreen(override val uiController: LoadingScreenController, private val eventBus: EventBus) : UiScreen(), EventHandler<AssetsLoadedEvent> {
    private val logger = fosLogger()

    override fun show() {
        super.show()
        eventBus.registerHandler(this)
    }

    override fun hide() {
        super.hide()
        eventBus.deregisterHandler(this)
    }

    override suspend fun handle(event: AssetsLoadedEvent) {
        logger.debug { "Handling asset loaded event" }

        onRenderingThread {
            logger.debug { "Loading complete, moving to main menu" }
            changeScreen<MainMenuScreen>()
            destroy()
        }
    }
}
