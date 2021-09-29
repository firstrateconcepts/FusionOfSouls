package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Application.LOG_ERROR
import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.firstrateconcepts.fusionofsouls.model.config.PlayerSettings
import java.nio.file.Paths

const val USER_DIR = "First Rate Concepts/Fusion Of Souls"
const val USER_SETTINGS_FILE = "fusion-of-souls-config.json"

// TODO: Backwards-compatibility capabilities for migrating settings files
// TODO: Unit testing this would be nice, but file-system specific stuff isn't the easiest thing to test. Maybe needs some refactoring first
class PlayerSettingsConfig {
    private val json = Json { prettyPrint = true }
    private val settingsDir by lazy { Paths.get(getDefaultPreferencesDirectory(), USER_DIR) }
    private val settingsFile by lazy { Lwjgl3FileHandle(settingsDir.resolve(USER_SETTINGS_FILE).toFile(), Files.FileType.Absolute) }
    private lateinit var settings: PlayerSettings

    fun get(): PlayerSettings {
        if (!::settings.isInitialized) {
            load()
        }

        return settings
    }

    private fun load() {
        if (!settingsDir.toFile().exists()) {
            settingsDir.toFile().mkdirs()
        }

        if (settingsFile.exists()) {
            settings = Json.decodeFromStream(settingsFile.read())
        } else {
            initDefaultSettings()
        }
    }

    fun save(settings: PlayerSettings) {
        settingsFile.writeString(json.encodeToString(settings), false)
    }

    private fun initDefaultSettings() {
        val primaryDisplayMode = Lwjgl3ApplicationConfiguration.getDisplayMode()
        val settings = PlayerSettings(fullscreen = true, vsync = true, logLevel = LOG_ERROR, resolution = PlayerSettings.Resolution(primaryDisplayMode.width, primaryDisplayMode.height))
        save(settings)
        this.settings = settings
    }

    // TODO: This is mostly pulled from https://github.com/libgdx/libgdx/pull/6614/files to put prefs in the right spot. Will be included in
    //  Lwjgl3ApplicationConfiguration in a future release.
    private fun getDefaultPreferencesDirectory() =
        if (UIUtils.isWindows) System.getenv("APPDATA") ?: ".prefs"
        else if (UIUtils.isMac) "Library/Preferences"
        else if (UIUtils.isLinux) ".config"
        else ".prefs"
}
