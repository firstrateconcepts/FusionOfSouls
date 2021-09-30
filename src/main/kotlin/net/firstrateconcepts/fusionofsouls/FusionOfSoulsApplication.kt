package net.firstrateconcepts.fusionofsouls

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import net.firstrateconcepts.fusionofsouls.config.ApplicationConfiguration
import net.firstrateconcepts.fusionofsouls.config.lazyInject
import net.firstrateconcepts.fusionofsouls.model.event.ExitRequest
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventHandler

class FusionOfSoulsApplication(
    game: FusionOfSoulsGame,
    configuration: ApplicationConfiguration,
    eventBus: EventBus
) : EventHandler<ExitRequest> {
    private val app by lazyInject<Application>()

    init {
        eventBus.registerHandler(this)
        Lwjgl3Application(game,  configuration)
    }

    override suspend fun handle(event: ExitRequest) = app.exit()
}
