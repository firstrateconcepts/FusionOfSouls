package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.math.Vector2
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.SteerableComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.steerable
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.TargetChangedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.action.MovementAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.AttackService
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.radDeg
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

val steeringFamily = oneOf(SteerableComponent::class).get()!!

// TODO: Unit tests
class SteeringSystem(
    private val engine: AsyncPooledEngine,
    private val actionQueueBus: ActionQueueBus,
    private val attackService: AttackService
) : IteratingSystem(steeringFamily) {
    private val logger = fosLogger()
    private val unitMoveCallbacks = mutableMapOf<Int, Entity.() -> Unit>()

    init {
        engine.addSystem(this)
        actionQueueBus.registerProcessor<MovementAction> { entity, action ->
            entity.steerable.applySteering(action.deltaTime, action.steeringOutput)
            unitMoveCallbacks[entity.id]?.invoke(entity)
        }
    }

    fun onUnitMove(unitId: Int, callback: Entity.() -> Unit) { unitMoveCallbacks[unitId] = callback }
    fun removeUnitMoveCallback(unitId: Int) = unitMoveCallbacks.remove(unitId)

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleComplete() = engine.runOnEngineThread {
        engine.getEntitiesFor(steeringFamily).forEach { entity ->
            entity.steerable.apply {
                position.set(initialPosition)
                rotation = initialRotation
            }
        }
    }

    @HandlesEvent
    fun targetChanged(event: TargetChangedEvent) = engine.runOnEngineThread {
        engine.withUnit(event.unitId) {
            val entity = this
            val behavior = entity.steerable.steeringBehavior

            if (event.newTarget == null) {
                logger.info { "Setting ${entity.name} to wander only" }
                behavior.resetToWanderOnly()
                return@withUnit
            }

            engine.withUnit(event.newTarget) {
                val target = this
                logger.info { "Setting ${entity.name} to target ${target.name}" }
                val targetSteerable = target.steerable
                behavior.changeTarget(targetSteerable)
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val steerable = entity.steerable
        val behavior = steerable.steeringBehavior

        if (attackService.canEntityAttack(entity)) {
            behavior.toggleMovement(false)
            steerable.linearVelocity.setZero()
        } else {
            behavior.toggleMovement(true)
        }

        val steeringOutput = SteeringAcceleration(Vector2())
        behavior.calculateSteering(steeringOutput)
        if (!steeringOutput.isZero) {
            actionQueueBus.addAction(MovementAction(entity.id, steeringOutput, deltaTime))
        }
    }

    private fun SteerableComponent.applySteering(time: Float, steeringOutput: SteeringAcceleration<Vector2>) {
        position.mulAdd(linearVelocity, time)
        linearVelocity.mulAdd(steeringOutput.linear, time).limit(maxLinearSpeed)
        rotation += angularVelocity.radDeg * time
        angularVelocity += steeringOutput.angular * time
    }
}
