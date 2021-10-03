package net.firstrateconcepts.fusionofsouls.util.framework.event

import com.badlogic.gdx.utils.Disposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

// TODO: There is possibly a world that we can avoid instead of ignore the unchecked cast, but this does the trick for now
// TODO: It'd be nice to unit test this, but unit testing multithreading is seriously tough.
//  Before any future modifications, this must become automatically tested.
// TODO: It's highly likely a situation will occur that we want to be able to stop event propagation.
//  At that point, it needs to be figured out how that should be handled here.
@Suppress("UNCHECKED_CAST")
class EventBus : Disposable {
    private val logger = fosLogger()
    private val asyncContext = newSingleThreadAsyncContext("EventBus-Loop")
    private val eventQueue = Channel<Event>()
    private val eventHandlers = mutableMapOf<KClass<out Event>, MutableList<EventHandler<Event>>>()
    private val handlerClasses = mutableSetOf<ClassHandlerMapping>()

    suspend fun <T : Event> enqueueEvent(event: T) {
        if (!eventQueue.isClosedForSend) {
            eventQueue.send(event)
        }
    }

    fun <T : Event> enqueueEventSync(event: T) {
        KtxAsync.launch(asyncContext) { enqueueEvent(event) }
    }

    fun registerHandlers(obj: Any) {
        if (handlerClasses.none { it.obj == obj }) {
            logger.info { "Initializing event handlers for ${obj::class.simpleName}" }
            handlerClasses.add(ClassHandlerMapping(obj))
        }

        handlerClasses.find { it.obj == obj }?.registerHandlers()
    }

    fun unregisterHandlers(obj: Any) {
        handlerClasses.find { it.obj == obj }?.unregisterHandlers()
    }

    fun <T : Event> registerHandler(eventType: KClass<T>, handler: EventHandler<T>) {
        eventHandlers.computeIfAbsent(eventType) { mutableListOf() } += handler as EventHandler<Event>
    }

    fun <T : Event> unregisterHandler(eventType: KClass<T>, handler: EventHandler<T>) {
        eventHandlers[eventType]?.remove(handler)
    }

    inline fun <reified T : Event> registerHandler(handler: EventHandler<T>) {
        registerHandler(T::class, handler)
    }

    inline fun <reified T : Event> unregisterHandler(handler: EventHandler<T>) {
        unregisterHandler(T::class, handler)
    }

    fun loop() {
        KtxAsync.launch(asyncContext) {
            logger.info { "Starting loop" }
            while (!eventQueue.isClosedForReceive) {
                eventQueue.receiveCatching().apply {
                    if (isFailure) {
                        return@launch
                    }

                    val event = getOrThrow()
                    eventHandlers[event::class]?.forEach {
                        logger.debug { "Handling event ${event.name}" }
                        it.handle(event)
                    }
                }
            }
        }
    }

    override fun dispose() {
        logger.info { "Disposing" }
        eventQueue.close()
        eventHandlers.clear()
        handlerClasses.clear()
        asyncContext.dispose()
    }

    private inner class ClassHandlerMapping(val obj: Any) {
        private val handlers: Map<KClass<out Event>, EventHandler<Event>>

        init {
            val handlers = mutableMapOf<KClass<out Event>, EventHandler<Event>>()

            obj::class.declaredMembers.filter { it.hasAnnotation<HandlesEvent>() }.forEach { fn ->
                val eventParam = fn.valueParameters.first()
                handlers[eventParam.type.jvmErasure as KClass<Event>] = eventHandler { event ->
                    if (fn.isSuspend) fn.callSuspend(obj, event) else fn.call(obj, event)
                }
            }

            logger.debug { "Found ${handlers.size} event handlers in ${obj::class.simpleName}" }
            this.handlers = handlers.toMap()
        }

        fun registerHandlers() {
            logger.info { "Registering event handlers for ${obj::class.simpleName}" }
            handlers.forEach {
                logger.debug { "Registering handler for ${it.key.simpleName} from ${obj::class.simpleName}" }
                registerHandler(it.key, it.value)
            }
        }

        fun unregisterHandlers() {
            logger.info { "Unregistering event handlers for ${obj::class.simpleName}" }
            handlers.forEach {
                logger.debug { "Unregistering handler for ${it.key.simpleName} from ${obj::class.simpleName}" }
                unregisterHandler(it.key, it.value)
            }
        }
    }
}
