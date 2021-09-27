package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import net.firstrateconcepts.fusionofsouls.model.config.PlayerSettings


class ApplicationConfiguration(settingsConfig: PlayerSettingsConfig) : Lwjgl3ApplicationConfiguration() {
    init {
        val settings = settingsConfig.get()
        setTitle("Fusion of Souls")
        handleResolution(settings.fullscreen, settings.resolution)
        useVsync(settings.vsync)
        setResizable(false)
    }

    private fun handleResolution(fullscreen: Boolean, resolution: PlayerSettings.Resolution) {
        if (fullscreen) {
            setFullscreenMode(getDisplayModes().find { it.width == resolution.width && it.height == resolution.height } ?: getDisplayMode())
        } else {
            resolution.apply { setWindowedMode(width, height) }
        }
    }
}
