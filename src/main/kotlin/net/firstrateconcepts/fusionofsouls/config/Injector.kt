package net.firstrateconcepts.fusionofsouls.config

import ktx.inject.Context
import ktx.inject.register
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame

inline fun <reified Type : Any> inject(): Type = Injector.inject()

object Injector : Context() {
    fun initialize() {
        register {
            bindSingleton<FusionOfSoulsGame>()
            bindSingleton<PlayerSettingsConfig>()
            bindSingleton<ApplicationConfiguration>()
        }
    }
}
