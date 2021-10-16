package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.ability
import net.firstrateconcepts.fusionofsouls.model.component.aliveUnitFamily
import net.firstrateconcepts.fusionofsouls.model.component.attackBonus
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.team
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityUsage
import net.firstrateconcepts.fusionofsouls.model.unit.ability.DamageAction
import net.firstrateconcepts.fusionofsouls.model.unit.ability.EffectAction
import net.firstrateconcepts.fusionofsouls.model.unit.ability.HealAction
import net.firstrateconcepts.fusionofsouls.model.unit.ability.TargetType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

// TODO: Mix strategy + factory for the action types, splitting out the "do" methods into individual classes
// TODO: Unit test individual strategy classes
class AbilityService(
    override val eventBus: EventBus,
    private val engine: AsyncPooledEngine,
    private val randomizer: RandomizerService,
    private val attackService: AttackService,
    private val effectService: EffectService
) : RunService() {
    private val logger = fosLogger()

    fun processEntityAbility(entity: Entity) {
        runOnServiceThread {
            logger.info { "Processing entity ability for entity [${entity.id}]" }
            with(entity.ability) {
                actions.forEach { usage ->
                    val targets = findTargets(entity, usage)
                    if (targets.isEmpty()) return@forEach

                    usage.actions.forEach { action ->
                        when (action) {
                            is DamageAction -> doDamage(entity, targets, action)
                            is HealAction -> doHeal(entity, targets, action)
                            is EffectAction -> doEffect(entity, targets, action)
                        }
                    }
                }
            }
        }
    }

    private fun doDamage(entity: Entity, targets: List<Entity>, action: DamageAction) {
        logger.info { "Doing damage from [${entity.name}] against [${targets.joinToString(",") { it.name }}] using action [$action]" }
        val rawRoll = randomizer.attackRoll()
        val attackBonus = entity.attrs.attackBonus()
        targets.forEach { target ->
            // TODO: Add in damage multiplier
            attackService.doAttack(entity, target, rawRoll, attackBonus.toInt())
        }
    }

    private fun doHeal(entity: Entity, targets: List<Entity>, action: HealAction) {
        logger.info { "Doing heal from [${entity.name}] against [${targets.joinToString(",") { it.name }}] using action [$action]" }
    }

    private fun doEffect(entity: Entity, targets: List<Entity>, action: EffectAction) {
        logger.info { "Doing effect from [${entity.name}] against [${targets.joinToString(",") { it.name }}] using action [${action.effect.name}]" }
        targets.forEach { target ->
            if (randomizer.percentChance(action.percentChance)) {
                effectService.addEffect(target, action.effect, action.duration)
            }
        }
    }

    // TODO: Support better targeting strategy
    private fun findTargets(self: Entity, usage: AbilityUsage): List<Entity> {
        // TODO: Filter untargetable
        val allTargets = mutableListOf<Entity>()

        if (usage.targetTypes.contains(TargetType.SELF)) {
            allTargets.add(self)
        }

        val aliveUnits = engine.getEntitiesFor(aliveUnitFamily)

        if (usage.targetTypes.contains(TargetType.ENEMY)) {
            allTargets.addAll(aliveUnits.filter { it.team != self.team })
        }

        if (usage.targetTypes.contains(TargetType.ALLY)) {
            allTargets.addAll(aliveUnits.filter { it.team == self.team })
        }

        val areaTargets = if (usage.area > 0) allTargets.filter { it.currentPosition.dst(self.currentPosition) <= usage.area } else allTargets
        return if (usage.targets > 0) areaTargets.shuffled(randomizer.rng).take(usage.targets) else areaTargets
    }
}
