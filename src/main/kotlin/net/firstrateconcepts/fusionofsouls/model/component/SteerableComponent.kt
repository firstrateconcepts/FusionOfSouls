package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.steer.SteerableAdapter
import com.badlogic.gdx.math.Vector2
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.UnitBlendedSteering
import net.firstrateconcepts.fusionofsouls.util.ext.degRad
import net.firstrateconcepts.fusionofsouls.util.ext.radDeg
import net.firstrateconcepts.fusionofsouls.util.ext.toAngle
import net.firstrateconcepts.fusionofsouls.util.ext.toVector

@Suppress("TooManyFunctions")
class SteerableComponent(val initialPosition: Vector2, val initialRotation: Float) : Component, SteerableAdapter<Vector2>() {
    private val position = initialPosition.cpy()!!
    var rotation: Float = initialRotation
    private val linearVelocity = Vector2()
    private val maxLinearSpeed = 0.75f
    private val maxLinearAcceleration = maxLinearSpeed * 2f
    private val maxAngularSpeed = 8f
    private val maxAngularAcceleration = maxAngularSpeed * 2f
    private var angularVelocity = 0f
    private val boundingRadius = 0.375f
    private var tagged = true
    
    val steeringBehavior = UnitBlendedSteering(this)

    override fun getPosition() = position
    override fun getOrientation() = rotation.degRad
    override fun setOrientation(orientation: Float) { rotation = orientation.radDeg }
    override fun getLinearVelocity() = linearVelocity
    override fun getAngularVelocity() = angularVelocity
    fun setAngularVelocity(value: Float) { angularVelocity = value }
    override fun getBoundingRadius() = boundingRadius
    override fun getMaxLinearSpeed() = maxLinearSpeed
    override fun getMaxLinearAcceleration() = maxLinearAcceleration
    override fun getMaxAngularSpeed() = maxAngularSpeed
    override fun getMaxAngularAcceleration() = maxAngularAcceleration
    override fun isTagged() = tagged
    override fun setTagged(tagged: Boolean) { this.tagged = tagged }
    override fun vectorToAngle(vector: Vector2) = vector.toAngle()
    override fun angleToVector(outVector: Vector2, angle: Float) = angle.toVector(outVector)
}

val steerableMapper = mapperFor<SteerableComponent>()
val Entity.steerable get() = this[steerableMapper]!!
val Entity.currentPosition get() = steerable.position
val Entity.rotation get() = steerable.rotation
val Entity.isInRangeOfTarget get() = steerable.steeringBehavior.target?.let { it.position.dst(this.currentPosition) <= attrs.attackRange() } ?: false

