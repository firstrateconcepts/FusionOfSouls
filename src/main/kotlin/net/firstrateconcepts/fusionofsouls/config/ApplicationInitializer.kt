package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import ktx.async.KtxAsync
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.service.asset.SkinLoader
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus


class ApplicationInitializer(
    private val eventBus: EventBus,
    private val assetLoader: AssetLoader,
    private val config: PlayerSettingsConfig,
    private val skinLoader: SkinLoader
) {
    fun initialize() {
        Gdx.app.logLevel = config.get().logLevel
        KtxAsync.initiate()

        TooltipManager.getInstance().apply {
            instant()
            animations = false
        }

        skinLoader.initializeSkin()
        eventBus.loop()
        assetLoader.load()
    }

    fun shutdown() {
        eventBus.dispose()
        assetLoader.dispose()
    }
}
