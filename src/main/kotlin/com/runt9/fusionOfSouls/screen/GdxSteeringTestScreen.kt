package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance
import com.badlogic.gdx.ai.steer.behaviors.Face
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering
import com.badlogic.gdx.ai.steer.behaviors.Pursue
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.runt9.fusionOfSouls.battleHeight
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.bigMargin
import ktx.scene2d.actors
import ktx.scene2d.vis.floatingGroup

class GdxSteeringTestScreen(override val stage: Stage) : FosScreen {
    override fun show() {
        val hero = addUnit("heroArrow-tp.png", 50f, 50f)
        val ally = addUnit("blueArrow-tp.png", 50f, 250f)
        val ally2 = addUnit("blueArrow-tp.png", 100f, 125f)
        val ally3 = addUnit("blueArrow-tp.png", 150f, 270f)
        val ally4 = addUnit("blueArrow-tp.png", 150f, 150f)
        val enemy = addUnit("redArrow-tp.png", 550f, 250f)
        val enemyAlly = addUnit("redArrow-tp.png", 450f, 100f)
        val enemyAlly2 = addUnit("redArrow-tp.png", 500f, 50f)
        val enemyAlly3 = addUnit("redArrow-tp.png", 450f, 50f)
        val enemyAlly4 = addUnit("redArrow-tp.png", 550f, 50f)

        val allies = listOf(hero, ally, ally2, ally3, ally4)
        val enemies = listOf(enemy, enemyAlly, enemyAlly2, enemyAlly3, enemyAlly4)
        val allUnits = allies + enemies

        allies.forEach {
            it.rotateBy(-90f)
            it.setTargets(enemies, allUnits)
        }
        enemies.forEach {
            it.rotation = 90f
            it.setTargets(allies, allUnits)
        }

        stage.actors {
            floatingGroup {
                width = battleWidth.toFloat() - bigMargin
                height = battleHeight.toFloat() - bigMargin

                allUnits.forEach(this::addActor)
                addActor(FpsLabel("FPS: ", VisUI.getSkin()))
            }
        }
    }

    override fun render(delta: Float) {
        GdxAI.getTimepiece().update(delta)
        super.render(delta)
    }

    fun addUnit(texture: String, x: Float, y: Float): SteeringActor {
        val unit = SteeringActor(TextureRegion(Texture(Gdx.files.internal(texture))))
        unit.setPosition(x, y)
        unit.maxLinearSpeed = 30f
        unit.maxLinearAcceleration = 100f
        return unit
    }
}

object Scene2dSteeringUtils {
    fun vectorToAngle(vector: Vector2): Float {
        return Math.atan2(-vector.x.toDouble(), vector.y.toDouble()).toFloat()
    }

    fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        outVector.x = (-Math.sin(angle.toDouble())).toFloat()
        outVector.y = Math.cos(angle.toDouble()).toFloat()
        return outVector
    }
}


class Scene2dLocation(private var position: Vector2 = Vector2(), private var orientation: Float = 0f) : Location<Vector2> {
    override fun newLocation(): Location<Vector2> {
        return Scene2dLocation()
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return Scene2dSteeringUtils.vectorToAngle(vector)
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return Scene2dSteeringUtils.angleToVector(outVector, angle)
    }

    override fun getPosition(): Vector2 {
        return position
    }

    override fun getOrientation(): Float {
        return orientation
    }

    override fun setOrientation(orientation: Float) {
        this.orientation = orientation
    }
}


class SteeringActor(var region: TextureRegion, var isIndependentFacing: Boolean = true) : Actor(), Steerable<Vector2> {
    private var position: Vector2
    private var linearVelocity: Vector2
    private var angularVelocity = 0f
//    lateinit var proximity: RadiusProximity<Vector2>
    lateinit var avoid: CollisionAvoidance<Vector2>
//    lateinit var castAvoid: RaycastObstacleAvoidance<Vector2>
    lateinit var pursue: Pursue<Vector2>
    lateinit var face: Face<Vector2>
    lateinit var look: LookWhereYouAreGoing<Vector2>
    lateinit var priority: PrioritySteering<Vector2>
    lateinit var blend: BlendedSteering<Vector2>

    lateinit var target: SteeringActor
    lateinit var targets: Iterable<SteeringActor>
    lateinit var allUnits: Iterable<SteeringActor>

    fun setTargets(targets: Iterable<SteeringActor>, allUnits: Iterable<SteeringActor>) {
        this.targets = targets
        this.allUnits = allUnits
        val target = targets.minByOrNull { position.dst(it.position) }!!
        initTarget(target)
    }

    fun initTarget(target: SteeringActor) {
        this.target = target
        val proximity = RadiusProximity(this, allUnits.filter { it != target }, 30f)
//        val proximity = FieldOfViewProximity(this, allUnits.filter { it != target }, 40f, MathUtils.degreesToRadians * 180)
        avoid = CollisionAvoidance(this, proximity)
        pursue = Pursue(this, target, 0.1f)
        face = Face(this, target).apply {
            timeToTarget = 0.1f
            alignTolerance = 0.001f
            decelerationRadius = MathUtils.degreesToRadians * 90
        }
        look = LookWhereYouAreGoing(this).apply {
            timeToTarget = 0.1f
            alignTolerance = 0.001f
            decelerationRadius = MathUtils.degreesToRadians * 90
        }
//        priority = PrioritySteering(this).apply {
//            add(look)
//            add(avoid)
//            add(pursue)
////            add(face)
//        }
        blend = BlendedSteering(this).apply {
            add(look, 20f)
            add(avoid, 10f)
            add(pursue, 3f)
            add(face, 1f)
        }
        steeringBehavior = blend
    }

    override fun getAngularVelocity(): Float {
        return angularVelocity
    }

    private var boundingRadius: Float
    override fun getBoundingRadius(): Float {
        return boundingRadius
    }

    private var tagged = false
    override fun setTagged(tagged: Boolean) {
        this.tagged = tagged
    }

    private var maxLinearSpeed = 100f
    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        this.maxLinearSpeed = maxLinearSpeed
    }

    override fun getMaxLinearSpeed(): Float {
        return maxLinearSpeed
    }

    private var maxLinearAcceleration = 200f
    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        this.maxLinearAcceleration = maxLinearAcceleration
    }

    override fun getMaxLinearAcceleration(): Float {
        return maxLinearAcceleration
    }

    private var maxAngularSpeed = 5f
    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        this.maxAngularSpeed = maxAngularSpeed
    }

    override fun getMaxAngularSpeed(): Float {
        return maxAngularSpeed
    }

    private var maxAngularAcceleration = 10f
    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        this.maxAngularAcceleration = maxAngularAcceleration
    }

    override fun getMaxAngularAcceleration(): Float {
        return maxAngularAcceleration
    }

    var steeringBehavior: SteeringBehavior<Vector2>? = null

    override fun getOrientation(): Float {
        return rotation * MathUtils.degreesToRadians
    }

    override fun setOrientation(orientation: Float) {
        rotation = orientation * MathUtils.radiansToDegrees
    }

    override fun isTagged(): Boolean {
        return tagged
    }

    override fun newLocation(): Location<Vector2> {
        return Scene2dLocation()
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return Scene2dSteeringUtils.vectorToAngle(vector)
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return Scene2dSteeringUtils.angleToVector(outVector, angle)
    }

    override fun getZeroLinearSpeedThreshold(): Float {
        return 0.001f
    }

    override fun setZeroLinearSpeedThreshold(value: Float) {
        throw UnsupportedOperationException()
    }

    override fun act(delta: Float) {
        position[getX(Align.center)] = getY(Align.center)

        val nearestTarget = targets.minByOrNull { it.position.dst(this@SteeringActor.position) }!!
        if (nearestTarget != target) {
            initTarget(nearestTarget)
        }

        steeringBehavior?.let { steer: SteeringBehavior<Vector2> ->
            val distance = target.position.dst(this@SteeringActor.position)
            if (distance <= 35) {
                pursue.isEnabled = false
//                avoid.isEnabled = false
                look.isEnabled = false
                linearVelocity.setZero()
            } else {
                pursue.isEnabled = true
                avoid.isEnabled = true
                look.isEnabled = true
            }

            steer.calculateSteering(steeringOutput)

            /*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 *
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

            // Apply steering acceleration
            applySteering(steeringOutput, delta)
            setPosition(position.x, position.y, Align.center)
        }
        super.act(delta)
    }

    private fun applySteering(steering: SteeringAcceleration<Vector2>, time: Float) {
        // Update position and linear velocity. Velocity is trimmed to maximum speed
        position.mulAdd(linearVelocity, time)
        linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed())

        // Update orientation and angular velocity
        if (isIndependentFacing) {
            rotation = rotation + angularVelocity * time * MathUtils.radiansToDegrees
            angularVelocity += steering.angular * time
        } else {
            // If we haven't got any velocity, then we can do nothing.
            if (!linearVelocity.isZero(zeroLinearSpeedThreshold)) {
                val newOrientation = vectorToAngle(linearVelocity)
                angularVelocity = (newOrientation - rotation * MathUtils.degreesToRadians) * time // this is superfluous if independentFacing is always true
                rotation = newOrientation * MathUtils.radiansToDegrees
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val color = color
        batch.setColor(color.r, color.g, color.b, parentAlpha)
        batch.draw(
            region, x, y, originX, originY, width, height, scaleX, scaleY,
            rotation + 90
        )
    }

    companion object {
        private val steeringOutput = SteeringAcceleration(Vector2())
    }

    init {
        position = Vector2()
        linearVelocity = Vector2()
        setBounds(0f, 0f, region.regionWidth.toFloat(), region.regionHeight.toFloat())
        boundingRadius = (region.regionWidth + region.regionHeight) / 4f
        this.setOrigin(region.regionWidth * .5f, region.regionHeight * .5f)
    }

    override fun getPosition(): Vector2 {
        return position
    }

    override fun getLinearVelocity(): Vector2 {
        return linearVelocity
    }
}

abstract class IntValueLabel(text: CharSequence, var oldValue: Int, style: LabelStyle?) :
    Label(text.toString() + oldValue, style) {
    var appendIndex: Int

    abstract val value: Int

    override fun act(delta: Float) {
        val newValue = value
        if (oldValue != newValue) {
            oldValue = newValue
            val sb = text
            sb.setLength(appendIndex)
            sb.append(oldValue)
            invalidateHierarchy()
        }
        super.act(delta)
    }

    init {
        appendIndex = text.length
    }
}

class FpsLabel(text: CharSequence?, style: LabelStyle?) : IntValueLabel(text!!, -1, style) {
    constructor(text: CharSequence?, skin: Skin) : this(text, skin[LabelStyle::class.java]) {}

    override val value: Int
        get() = Gdx.graphics.framesPerSecond
}
