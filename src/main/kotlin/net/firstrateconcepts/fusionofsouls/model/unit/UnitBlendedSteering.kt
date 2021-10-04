package net.firstrateconcepts.fusionofsouls.model.unit

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance
import com.badlogic.gdx.ai.steer.behaviors.Face
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering
import com.badlogic.gdx.ai.steer.behaviors.Pursue
import com.badlogic.gdx.ai.steer.behaviors.Separation
import com.badlogic.gdx.ai.steer.behaviors.Wander
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity
import com.badlogic.gdx.math.Vector2
import ktx.collections.gdxArrayOf
import net.firstrateconcepts.fusionofsouls.util.ext.WallAvoid
import net.firstrateconcepts.fusionofsouls.util.ext.degRad

// TODO: This realistically needs pathfinding + steering, but this works really well for now
class UnitBlendedSteering(unit: Steerable<Vector2>) : BlendedSteering<Vector2>(unit) {
    var target: Steerable<Vector2>? = null
    val proximityAgents = mutableSetOf<Steerable<Vector2>>()
    private val proximity by lazy { RadiusProximity(owner, proximityAgents, 0.75f) }
    private val avoid by lazy { CollisionAvoidance(owner, proximity) }
    private val wallAvoid by lazy { WallAvoid(owner) }
    private val separate by lazy { Separation(owner, proximity) }
    private val pursue by lazy { Pursue(owner, target, 0.1f) }
    private val face by lazy {
        Face(owner, target).apply {
            timeToTarget = 0.1f
            alignTolerance = 0.001f
            decelerationRadius = 90f.degRad
        }
    }
    private val look by lazy {
        LookWhereYouAreGoing(owner).apply {
            timeToTarget = 0.1f
            alignTolerance = 0.001f
            decelerationRadius = 90f.degRad
        }
    }
    private val wanderOnly by lazy { BehaviorAndWeight(Wander(owner), 1f) }
    private val movingBehavior by lazy {
        gdxArrayOf(
            PrioritySteering(owner).apply {
                add(BlendedSteering(owner).apply {
                    add(avoid weight 1f)
                    add(wallAvoid weight 1f)
                })
                add(pursue)
                add(separate)
                add(face)
            } weight 1f,
            look weight 1f
        )
    }

    init {
        resetToWanderOnly()
    }

    fun resetToWanderOnly() {
        clear()
        add(wanderOnly)
        target = null
    }

    fun changeTarget(target: Steerable<Vector2>) {
        val previousTarget = this.target
        this.target = target
        pursue.target = target
        face.target = target
        previousTarget?.let { proximityAgents.add(it) }
        proximityAgents.remove(target)

        if (list.contains(wanderOnly)) {
            list.clear()
            list.addAll(movingBehavior)
        }
    }

    fun toggleMovement(isMoving: Boolean) {
        pursue.isEnabled = isMoving
        look.isEnabled = isMoving
        avoid.isEnabled = isMoving
        wallAvoid.isEnabled = isMoving
        separate.isEnabled = isMoving
        face.isEnabled = !isMoving
    }

    private fun clear() = list.clear()
    private infix fun SteeringBehavior<Vector2>.weight(weight: Float) = BehaviorAndWeight(this, weight)
}
