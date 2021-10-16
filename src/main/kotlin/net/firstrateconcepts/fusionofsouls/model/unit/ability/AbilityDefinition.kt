package net.firstrateconcepts.fusionofsouls.model.unit.ability

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import net.firstrateconcepts.fusionofsouls.model.unit.effect.definition.EffectDefinition

// TODO: Area should actually be area type because cone is an option
// TODO: Targeting needs to be a targeting strategy for things like "lowest health enemies" or "two lowest HP allies"
interface AbilityDefinition {
    val id: Int
    val name: String
    val description: String
    val cooldown: Float
    val animation: SequenceAction
    val actions: List<AbilityUsage>
}

enum class TargetType { SELF, ENEMY, ALLY }
interface AbilityAction
data class DamageAction(val damageMultiplier: Float) : AbilityAction
data class HealAction(val healMultiplier: Float) : AbilityAction
data class EffectAction(val effect: EffectDefinition, val duration: Float, val percentChance: Float = 1f) : AbilityAction

data class AbilityUsage(
    val area: Float,
    val targets: Int,
    val targetTypes: Set<TargetType>,
    val actions: List<AbilityAction>
)
