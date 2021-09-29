package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.kotcrab.vis.ui.VisUI
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class ApplicationInitializer(private val eventBus: EventBus, private val assetLoader: AssetLoader, private val config: PlayerSettingsConfig) {
    fun initialize() {
        Gdx.app.logLevel = config.get().logLevel
        KtxAsync.initiate()
        VisUI.load(Gdx.files.classpath("skin/star-soldier-ui.json"))
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        TooltipManager.getInstance().apply {
            instant()
            animations = false
        }
        eventBus.loop()
        assetLoader.load()
    }

    fun shutdown() {
        eventBus.dispose()
        assetLoader.dispose()
        VisUI.dispose()
    }
}
