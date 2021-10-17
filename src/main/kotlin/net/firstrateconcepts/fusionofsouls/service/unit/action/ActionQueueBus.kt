package net.firstrateconcepts.fusionofsouls.service.unit.action

import com.badlogic.ashley.core.Entity
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.ashley.allOf
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.unit.ActionsComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.actions
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitActionType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

private val actionsFamily = allOf(UnitComponent::class, AliveComponent::class, ActionsComponent::class).get()!!

class ActionQueueBus(eventBus: EventBus, registry: RunServiceRegistry, private val engine: AsyncPooledEngine) : RunService(eventBus, registry) {
    private val logger = fosLogger()
    private val asyncContext = newSingleThreadAsyncContext("ActionQueue-Thread")
    private val queueLoopMap = mutableMapOf<Int, Job>()

    @HandlesEvent(BattleStartedEvent::class)
    fun initialize() = engine.getEntitiesFor(actionsFamily).forEach {
        queueLoopMap[it.id] = unitJob(it)
    }

    fun addAction(entity: Entity, action: UnitAction) = KtxAsync.launch { entity.actions.queue.send(action) }
    fun addAction(entity: Entity, type: UnitActionType, callback: () -> Unit) = addAction(entity, UnitAction(type, callback))

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleStop() {
        queueLoopMap.values.forEach(Job::cancel)
        queueLoopMap.clear()
    }

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
                if (actions.blockers.any { it.action == action.type }) {
                    addAction(entity, action)
                    return@apply
                }

                action.callback()
            }
        }
    }
}
