package net.firstrateconcepts.fusionofsouls.model.unit.action

import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.math.Vector2
import kotlin.reflect.KClass

interface UnitAction {
    val unitId: Int
}

class MovementAction(override val unitId: Int, val steeringOutput: SteeringAcceleration<Vector2>, val deltaTime: Float) : UnitAction
class TargetAction(override val unitId: Int, val newTarget: Int) : UnitAction
class AttackAction(override val unitId: Int) : UnitAction
class AbilityAction(override val unitId: Int) : UnitAction

class ActionBlocker(val source: Any, val action: KClass<out UnitAction>)
