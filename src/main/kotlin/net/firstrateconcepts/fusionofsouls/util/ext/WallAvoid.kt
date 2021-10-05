package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.math.Vector2
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_HEIGHT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_WIDTH

// TODO: Unit tests. Also probably could use some fine-tuning
@Suppress("ComplexCondition")
class WallAvoid(owner: Steerable<Vector2>) : SteeringBehavior<Vector2>(owner) {
    override fun calculateRealSteering(steering: SteeringAcceleration<Vector2>): SteeringAcceleration<Vector2> {
        val position = owner.position
        if (position.x > 0f && position.x < GAME_WIDTH - 1 && position.y > 0f && position.y < GAME_HEIGHT - 1) {
            steering.setZero()
        } else {
            val linear = steering.linear
            if (position.x <= 0f) linear.x = 1f else if (position.x >= GAME_WIDTH - 1) linear.x = -1f
            if (position.y <= 0f) linear.y = 1f else if (position.y >= GAME_HEIGHT - 1) linear.y = -1f
        }

        return steering
    }
}
