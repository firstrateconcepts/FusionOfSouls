package net.firstrateconcepts.fusionofsouls.util.framework.ui

import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.model.event.ShowDialogRequest
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventHandler
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.FosStage

class DialogManager(private val eventBus: EventBus) : EventHandler<ShowDialogRequest<*>>, Disposable {
    var currentStage: FosStage? = null

    init {
        eventBus.registerHandler(this)
    }

    override suspend fun handle(event: ShowDialogRequest<*>) {
        currentStage?.run {
            val dialog = Injector.getProvider(event.dialogClass.java)()
            dialog.show(this)
        }
    }

    override fun dispose() {
        eventBus.deregisterHandler(this)
    }
}
