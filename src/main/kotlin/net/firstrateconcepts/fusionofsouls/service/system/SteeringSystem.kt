package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.math.Vector2
import ktx.ashley.configureEntity
import ktx.ashley.oneOf
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.component.AttackingComponent
import net.firstrateconcepts.fusionofsouls.model.component.SteerableComponent
import net.firstrateconcepts.fusionofsouls.model.component.attackRange
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.isAttacking
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.steerable
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.model.event.TargetChangedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.radDeg
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

val steeringFamily = oneOf(SteerableComponent::class).get()!!

// TODO: Unit tests
class SteeringSystem(private val engine: AsyncPooledEngine) : IteratingSystem(steeringFamily) {
    private val logger = fosLogger()

    // NB: This isn't thread-safe, but this is single-threaded for now, so we're good
    private val steeringOutput = SteeringAcceleration(Vector2())

    init {
        engine.addSystem(this)
    }

    @HandlesEvent
    fun battleComplete(runStatusChanged: RunStatusChanged) {
        if (runStatusChanged.newStatus != BattleStatus.AFTER_BATTLE) return

        engine.runOnEngineThread {
            engine.getEntitiesFor(steeringFamily).forEach { entity ->
                entity.steerable.apply {
                    position.set(initialPosition)
                    rotation = initialRotation
                }
            }
        }
    }

    @HandlesEvent
    fun targetChanged(event: TargetChangedEvent) = engine.runOnEngineThread {
        engine.findById(event.unitId)?.let { entity ->
            val behavior = entity.steerable.steeringBehavior

            if (event.newTarget == null) {
                logger.info { "Setting ${entity.name} to wander only" }
                behavior.resetToWanderOnly()
                return@let
            }

            engine.findById(event.newTarget)?.let { target ->
                logger.info { "Setting ${entity.name} to target ${target.name}" }
                val targetSteerable = target.steerable
                behavior.changeTarget(targetSteerable)
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val steerable = entity.steerable
        val behavior = steerable.steeringBehavior
        val target = behavior.target
        val range = entity.attrs.attackRange()


        if (target != null && entity.currentPosition.dst(target.position) <= range) {
            if (!entity.isAttacking) {
                logger.info { "Setting ${entity.name} to attacking" }
                engine.configureEntity(entity) { with<AttackingComponent>() }
                behavior.toggleMovement(false)
                steerable.linearVelocity.setZero()
            }
        } else {
            behavior.toggleMovement(true)
        }

        behavior.calculateSteering(steeringOutput)
        steerable.applySteering(deltaTime)
    }

    private fun SteerableComponent.applySteering(time: Float) {
        position.mulAdd(linearVelocity, time)
        linearVelocity.mulAdd(steeringOutput.linear, time).limit(maxLinearSpeed)
        rotation += angularVelocity.radDeg * time
        angularVelocity += steeringOutput.angular * time
    }
}
