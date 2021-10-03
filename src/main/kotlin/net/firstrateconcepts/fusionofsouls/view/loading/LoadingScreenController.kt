package net.firstrateconcepts.fusionofsouls.view.loading

import ktx.assets.async.AssetStorage
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.model.event.AssetsLoadedEvent
import net.firstrateconcepts.fusionofsouls.model.event.enqueueChangeScreen
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.percent
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.UiScreenController
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController

class LoadingScreenController(private val assets: AssetStorage, private val eventBus: EventBus) : UiScreenController() {
    private val logger = fosLogger()
    override val vm = LoadingScreenViewModel()
    override val view = LoadingScreenView(this, vm)

    override fun show() {
        super.show()
        eventBus.registerHandlers(this)
    }

    override fun render(delta: Float) {
        super.render(delta)
        assets.progress.run {
            logger.debug { "Asset loading status: $loaded / $total (${percent.percent()}%)" }
            vm.loadingPercent(percent)
        }
    }

    override fun hide() {
        super.hide()
        eventBus.unregisterHandlers(this)
    }

    @HandlesEvent
    @Suppress("UnusedPrivateMember")
    suspend fun handle(event: AssetsLoadedEvent) = onRenderingThread {
        logger.debug { "Loading complete, moving to main menu" }
        eventBus.enqueueChangeScreen<MainMenuScreenController>()
    }
}
