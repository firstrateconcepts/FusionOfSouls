package net.firstrateconcepts.fusionofsouls

import ktx.app.KtxGame
import ktx.app.KtxScreen
import net.firstrateconcepts.fusionofsouls.config.ApplicationInitializer
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreen
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen

class FusionOfSoulsGame : KtxGame<KtxScreen>() {
    override fun create() {
        Injector.initGdxDeps()
        Injector.initRemainingDeps()

        inject<ApplicationInitializer>().initialize()

        addScreen(inject<LoadingScreen>())
        addScreen(inject<MainMenuScreen>())
        setScreen<LoadingScreen>()
    }
}
