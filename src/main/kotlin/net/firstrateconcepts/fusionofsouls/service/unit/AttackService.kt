package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.component.attackBonus
import net.firstrateconcepts.fusionofsouls.model.component.attackSpeed
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.critBonus
import net.firstrateconcepts.fusionofsouls.model.component.defense
import net.firstrateconcepts.fusionofsouls.model.component.evasion
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.isInRangeOfTarget
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.unit.currentHp
import net.firstrateconcepts.fusionofsouls.model.component.unit.hasTarget
import net.firstrateconcepts.fusionofsouls.model.component.unit.target
import net.firstrateconcepts.fusionofsouls.model.component.unit.timerInfo
import net.firstrateconcepts.fusionofsouls.model.component.unit.timers
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.DamageRequest
import net.firstrateconcepts.fusionofsouls.model.unit.HitCheck
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorScope
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitActionType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.service.system.TimersSystem
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class AttackService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val actionQueueBus: ActionQueueBus,
    private val interactionService: UnitInteractionService,
    private val timersSystem: TimersSystem,
    private val attributeService: AttributeService,
    private val engine: AsyncPooledEngine,
    private val randomizer: RandomizerService,
    private val unitCommunicator: UnitCommunicator
) : RunService(eventBus, registry) {
    private val logger = fosLogger()

    fun canEntityAttack(entity: Entity): Boolean {
        if (!entity.hasTarget) return false

        return entity.isInRangeOfTarget
    }

    private fun processEntityAttack(attacker: Entity) {
        if (!canEntityAttack(attacker)) return

        engine.withUnit(attacker.target!!) { defender ->
            runOnServiceThread {
                val rawRoll = randomizer.attackRoll()
                val attackBonus = attacker.attrs.attackBonus()
                doAttack(attacker, defender, rawRoll, attackBonus)
            }
        }
    }

    private fun doAttack(attacker: Entity, defender: Entity, rawRoll: Int, attackBonus: Float) {
        val tag = "[${attacker.name} -> ${defender.name}]: "
        val combatStr = StringBuilder(tag)

        val evasion = defender.attrs.evasion()
        val hitResult = interactionService.hitCheck(InterceptorScope.ATTACK, attacker, defender, HitCheck(rawRoll, attackBonus, evasion))

        combatStr.append("Rolled [$rawRoll] | $attackBonus Bonus vs $evasion Evasion | Total Roll [${hitResult.finalRoll}]: ")

        if (!hitResult.isHit) {
            combatStr.append("Attack missed!")
            logger.info { combatStr.toString() }
            return
        }

        combatStr.append("Attack hits! ")
        if (hitResult.isCrit) {
            combatStr.append("CRITICAL HIT! Crit multi [${attacker.attrs.critBonus()}] ")
        }

        val damage = interactionService.damage(InterceptorScope.ATTACK, attacker, defender, DamageRequest(hitResult))

        combatStr.append("Damage Data: [Damage Scale: ${hitResult.damageScale} | Raw Damage: ${damage.rawDamage} | Defense: ${defender.attrs.defense} |")
            .append(" Final: ${damage.finalDamage} | Defender HP: ${defender.currentHp} / ${defender.attrs.maxHp()}]")
        logger.info { combatStr.toString() }
    }

    @HandlesEvent
    fun unitActivated(event: UnitActivatedEvent) = engine.withUnit(event.unitId) { addAttackTimer(it) }

    // TODO: Basically the same as ability service one, see if can make same function
    private fun addAttackTimer(entity: Entity) {
        logger.info { "Adding attack timer for ${entity.id}" }
        val timerId = entity.timerInfo.newTimer(1f / entity.attrs.attackSpeed())
        val timer = entity.timers[timerId]!!

        attributeService.onChange(entity.id, AttributeType.ATTACK_SPEED) { _, attr -> timer.targetTime = 1f / attr() }

        timersSystem.onTimerReady(timerId) {
            if (!canEntityAttack(entity)) return@onTimerReady

            logger.info { "Pausing attack timer for ${entity.id}" }
            entity.timers[timerId]?.pause()

            actionQueueBus.addAction(entity, UnitActionType.ATTACK) {
                unitCommunicator.getUnit(entity.id)?.attack {
                    logger.info { "Processing attack for ${entity.id}" }
                    processEntityAttack(entity)
                    timer.reset()
                    timer.resume()
                }
            }
        }

        timersSystem.onTimerTick(timerId) { unitCommunicator.getUnit(entity.id)?.updateAttackTimer(timer.percentComplete) }
    }
}
