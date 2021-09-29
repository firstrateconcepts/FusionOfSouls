package net.firstrateconcepts.fusionofsouls

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import net.firstrateconcepts.fusionofsouls.config.ApplicationInitializer
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreen
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen

class FusionOfSoulsGame : KtxGame<KtxScreen>() {
    private val initializer by lazy { inject<ApplicationInitializer>() }

    override fun create() {
        initializer.initialize()

        Injector.initGdxDeps()
        Injector.initRunningDeps()

        inject<Input>().inputProcessor = inject<InputMultiplexer>()

        addScreen(inject<LoadingScreen>())
        addScreen(inject<MainMenuScreen>())
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        initializer.shutdown()
    }
}
