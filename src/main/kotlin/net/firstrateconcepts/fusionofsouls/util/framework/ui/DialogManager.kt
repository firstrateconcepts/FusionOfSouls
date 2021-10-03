package net.firstrateconcepts.fusionofsouls.util.framework.ui

import com.badlogic.gdx.utils.Disposable
import ktx.async.onRenderingThread
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.model.event.ShowDialogRequest
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.FosStage

class DialogManager(private val eventBus: EventBus) : Disposable {
    var currentStage: FosStage? = null

    init {
        eventBus.registerHandlers(this)
    }

    @HandlesEvent
    suspend fun showDialog(event: ShowDialogRequest<*>) = onRenderingThread {
        currentStage?.run {
            val dialog = Injector.getProvider(event.dialogClass.java)()
            dialog.show(this)
        }
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
    }
}
