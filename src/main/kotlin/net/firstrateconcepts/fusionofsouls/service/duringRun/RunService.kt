package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.gdx.utils.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.util.framework.event.Event
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventHandler

@Suppress("LeakingThis")
abstract class RunService : Disposable {
    private val eventBus = inject<EventBus>()
    protected val serviceContext = newSingleThreadAsyncContext("Service-Thread")
    val handlers = mutableListOf<EventHandler<Event>>()

    init {
        inject<RunServiceRegistry>().register(this)
    }

    fun start() {
        eventBus.registerHandlers(this)
        startInternal()
    }

    fun stop() {
        eventBus.unregisterHandlers(this)
        stopInternal()
    }

    fun runOnServiceThread(block: suspend CoroutineScope.() -> Unit) = KtxAsync.launch(serviceContext, block = block)

    protected open fun startInternal() = Unit
    protected open fun stopInternal() = Unit

    open fun frame(delta: Float) = Unit
    override fun dispose() = Unit
}
