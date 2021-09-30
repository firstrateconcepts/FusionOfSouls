package net.firstrateconcepts.fusionofsouls

import net.firstrateconcepts.fusionofsouls.config.Injector

object FusionOfSoulsLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Injector.initStartupDeps()
        Injector.newInstanceOf<FusionOfSoulsApplication>()
    }
}
