package net.firstrateconcepts.fusionofsouls

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.config.ApplicationInitializer
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.config.lazyInject
import net.firstrateconcepts.fusionofsouls.model.event.ChangeScreenRequest
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.FosScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.DuringRunScreen
import net.firstrateconcepts.fusionofsouls.view.heroSelect.HeroSelectScreenController
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreenController
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController

class FusionOfSoulsGame : KtxGame<KtxScreen>(), EventHandler<ChangeScreenRequest<*>> {
    private val logger = fosLogger()
    private val initializer by lazyInject<ApplicationInitializer>()
    private val input by lazyInject<Input>()
    private val eventBus by lazyInject<EventBus>()

    override fun create() {
        initializer.initialize()

        Injector.initGdxDeps()
        Injector.initRunningDeps()

        input.inputProcessor = inject<InputMultiplexer>()
        eventBus.registerHandler(this)

        addScreen<LoadingScreenController>()
        addScreen<MainMenuScreenController>()
        addScreen<HeroSelectScreenController>()
        addScreen<DuringRunScreen>()
        setScreen<LoadingScreenController>()
    }

    override fun dispose() {
        eventBus.deregisterHandler(this)
        super.dispose()
        initializer.shutdown()
    }

    override suspend fun handle(event: ChangeScreenRequest<*>) = onRenderingThread {
        logger.debug { "Changing screen to ${event.screenClass.simpleName}" }
        setScreen(event.screenClass.java)
    }

    private inline fun <reified S : FosScreen> addScreen() = addScreen(inject<S>())
}
