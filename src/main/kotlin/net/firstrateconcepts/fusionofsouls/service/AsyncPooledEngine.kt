package net.firstrateconcepts.fusionofsouls.service

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

// TODO: Looks like we need a thread-safe version of the Family Manager
class AsyncPooledEngine(private val eventBus: EventBus) : PooledEngine(10, 100, 25, 1000) {
    private val asyncContext = newSingleThreadAsyncContext("Engine-Thread")

    fun runOnEngineThread(block: suspend CoroutineScope.() -> Unit) = KtxAsync.launch(asyncContext, block = block)

    override fun update(deltaTime: Float) { runOnEngineThread { super.update(deltaTime) } }

    override fun addSystem(system: EntitySystem) {
        eventBus.registerHandlers(system)
        super.addSystem(system)
    }

    override fun removeSystem(system: EntitySystem) {
        eventBus.unregisterHandlers(system)
        super.removeSystem(system)
    }

    override fun removeAllSystems() {
        systems.forEach(eventBus::unregisterHandlers)
        super.removeAllSystems()
    }
}
