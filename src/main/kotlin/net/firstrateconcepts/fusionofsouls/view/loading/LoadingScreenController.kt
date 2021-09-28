package net.firstrateconcepts.fusionofsouls.view.loading

import ktx.actors.centerPosition
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.event.EventBus
import net.firstrateconcepts.fusionofsouls.event.EventHandler
import net.firstrateconcepts.fusionofsouls.model.event.AssetsLoadedEvent
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger

class LoadingScreenController(private val assets: AssetStorage, private val eventBus: EventBus) : EventHandler<AssetsLoadedEvent> {
    private val logger = fosLogger()
    private var loadingComplete = false
    var onLoadingComplete: (() -> Unit)? = null
    val view = LoadingScreenView(this)

    fun addedToStage() {
        eventBus.registerHandler(this)
        view.viewDefinition.centerPosition()
    }

    fun removedFromStage() {
        eventBus.deregisterHandler(this)
        onLoadingComplete = null
    }

    fun render(delta: Float) {
        if (!loadingComplete) {
            assets.progress.run {
                logger.debug { "Asset loading status: $loaded / $total (${percent * 100}%)" }
            }
        }
    }

    override suspend fun handle(event: AssetsLoadedEvent) {
        logger.debug { "Handling asset loaded event" }
        onLoadingComplete?.invoke()
    }
}
