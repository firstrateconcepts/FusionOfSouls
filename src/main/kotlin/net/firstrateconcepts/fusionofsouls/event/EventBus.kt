package net.firstrateconcepts.fusionofsouls.event

import com.badlogic.gdx.utils.Disposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger

// TODO: There is possibly a world that we can avoid instead of ignore the unchecked cast, but this does the trick for now
class EventBus : Disposable {
    private val log = fosLogger()
    private val asyncContext = newSingleThreadAsyncContext("EventBus-Loop")
    private val eventQueue = Channel<Event>()
    private val eventHandlers = mutableListOf<EventHandler<Event>>()

    suspend fun <T : Event> postEvent(event: T) {
        log.debug { "Posting event ${event.name}" }
        eventQueue.send(event)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Event> registerHandler(handler: EventHandler<T>) {
        eventHandlers.add(handler as EventHandler<Event>)
    }

    fun <T : Event> deregisterHandler(handler: EventHandler<T>) {
        eventHandlers.remove(handler)
    }

    fun loop() {
        log.info { "Starting loop" }
        KtxAsync.launch(asyncContext) {
            while (!eventQueue.isClosedForReceive) {
                eventQueue.receiveCatching().apply {
                    if (isFailure) {
                        return@launch
                    }

                    val event = getOrThrow()
                    log.debug { "Received ${event.name} from queue" }
                    eventHandlers.filter { it.canHandleEvent(event) }.forEach { it.handle(event) }
                }
            }
        }
    }

    override fun dispose() {
        log.info { "Disposing" }
        eventQueue.close()
        eventHandlers.clear()
        asyncContext.dispose()
    }
}
