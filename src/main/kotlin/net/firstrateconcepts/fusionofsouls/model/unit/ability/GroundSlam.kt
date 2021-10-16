package net.firstrateconcepts.fusionofsouls.model.unit.ability

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.actors.then
import net.firstrateconcepts.fusionofsouls.model.unit.effect.definition.StunDefinition

object GroundSlam : AbilityDefinition {
    private val usage = AbilityUsage(
        area = 1f,
        targets = 0,
        targetTypes = setOf(TargetType.ENEMY),
        actions = listOf(DamageAction(1.25f), EffectAction(StunDefinition, 1.5f, 0.25f))
    )
    private const val radius = 1

    override val id = 1
    override val name = "Ground Slam"
    override val description = "Unit smacks the ground, damaging all enemies in a radius of $radius. Units hit by this ability have a 25% chance to be stunned."
    override val cooldown = 5f
    override val animation = Actions.scaleBy(1.01f, 1.01f, 0.1f) then Actions.scaleTo(1f, 1f, 0.1f)
    override val actions = listOf(usage)
}

