package net.firstrateconcepts.fusionofsouls.util.framework.event

import com.badlogic.gdx.utils.Disposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger

// TODO: There is possibly a world that we can avoid instead of ignore the unchecked cast, but this does the trick for now
// TODO: It'd be nice to unit test this, but unit testing multithreading is seriously tough.
//  Before any future modifications, this must become automatically tested.
// TODO: It's highly likely a situation will occur that we want to be able to stop event propagation.
//  At that point, it needs to be figured out how that should be handled here.
class EventBus : Disposable {
    private val logger = fosLogger()
    private val asyncContext = newSingleThreadAsyncContext("EventBus-Loop")
    private val eventQueue = Channel<Event>()
    private val eventHandlers = mutableListOf<EventHandler<Event>>()

    suspend fun <T : Event> postEvent(event: T) {
        logger.debug { "Posting event ${event.name}" }
        if (!eventQueue.isClosedForSend) {
            eventQueue.send(event)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Event> registerHandler(handler: EventHandler<T>) {
        eventHandlers.add(handler as EventHandler<Event>)
    }

    fun <T : Event> deregisterHandler(handler: EventHandler<T>) {
        eventHandlers.remove(handler)
    }

    fun loop() {
        logger.info { "Starting loop" }
        KtxAsync.launch(asyncContext) {
            while (!eventQueue.isClosedForReceive) {
                eventQueue.receiveCatching().apply {
                    if (isFailure) {
                        return@launch
                    }

                    val event = getOrThrow()
                    logger.debug { "Received ${event.name} from queue" }
                    eventHandlers.filter { it.canHandleEvent(event) }.forEach { it.handle(event) }
                }
            }
        }
    }

    override fun dispose() {
        logger.info { "Disposing" }
        eventQueue.close()
        eventHandlers.clear()
        asyncContext.dispose()
    }
}
