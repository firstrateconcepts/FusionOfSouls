package net.firstrateconcepts.fusionofsouls

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import net.firstrateconcepts.fusionofsouls.config.ApplicationConfiguration
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.config.PlayerSettingsConfig
import net.firstrateconcepts.fusionofsouls.config.inject

object FusionOfSoulsLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Injector.initStartupDeps()
        Lwjgl3Application(inject<FusionOfSoulsGame>(), inject<ApplicationConfiguration>()).apply {
            val settings = inject<PlayerSettingsConfig>()
            logLevel = settings.get().logLevel
        }
    }
}
