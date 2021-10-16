package net.firstrateconcepts.fusionofsouls.service.unit.action

import com.badlogic.ashley.core.Entity
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.ashley.allOf
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.model.component.ActionsComponent
import net.firstrateconcepts.fusionofsouls.model.component.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.component.actions
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

private val actionsFamily = allOf(UnitComponent::class, AliveComponent::class, ActionsComponent::class).get()!!

class ActionQueueBus(override val eventBus: EventBus, private val engine: AsyncPooledEngine) : RunService() {
    private val logger = fosLogger()
    private val asyncContext = newSingleThreadAsyncContext("ActionQueue-Thread")
    private val queueLoopMap = mutableMapOf<Int, Job>()
    val actionProcessors = mutableMapOf<KClass<out UnitAction>, ActionProcessor<in UnitAction>>()

    @HandlesEvent(BattleStartedEvent::class)
    fun initialize() = engine.getEntitiesFor(actionsFamily).forEach {
        queueLoopMap[it.id] = unitJob(it)
    }

    fun addAction(action: UnitAction) = KtxAsync.launch { engine.withUnit(action.unitId) { it.actions.queue.send(action) } }

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleStop() {
        queueLoopMap.values.forEach(Job::cancel)
        queueLoopMap.clear()
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified A : UnitAction> registerProcessor(processor: ActionProcessor<A>) {
        actionProcessors[A::class] = processor as ActionProcessor<in UnitAction>
    }

    inline fun <reified A : UnitAction> registerProcessor(crossinline handler: (Entity, A) -> Unit) = registerProcessor(actionProcessor(handler))

    private fun unitJob(entity: Entity) = KtxAsync.launch(asyncContext) {
        val actions = entity.actions
        logger.info { "Starting action loop for unit [${entity.id}]" }
        while (!actions.queue.isClosedForReceive && this.isActive) {
            actions.queue.receiveCatching().apply {
                if (isFailure) {
                    logger.error { "Entity [${entity.id}] action from queue errored out: ${exceptionOrNull()}" }
                    return@apply
                }

                val action = getOrThrow()
                if (actions.blockers.any { action::class.isSubclassOf(it.action) }) {
                    addAction(action)
                    return@apply
                }

                actionProcessors[action::class]?.process(action)
            }
        }
    }
}
