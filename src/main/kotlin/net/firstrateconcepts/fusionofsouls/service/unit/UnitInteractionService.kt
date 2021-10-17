package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.attackBonus
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.baseDamage
import net.firstrateconcepts.fusionofsouls.model.component.critBonus
import net.firstrateconcepts.fusionofsouls.model.component.defense
import net.firstrateconcepts.fusionofsouls.model.component.evasion
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.isInRangeOfTarget
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.unit.currentHp
import net.firstrateconcepts.fusionofsouls.model.component.unit.hasTarget
import net.firstrateconcepts.fusionofsouls.model.component.unit.interceptAsTarget
import net.firstrateconcepts.fusionofsouls.model.component.unit.interceptAsUnit
import net.firstrateconcepts.fusionofsouls.model.component.unit.target
import net.firstrateconcepts.fusionofsouls.model.unit.DamageRequest
import net.firstrateconcepts.fusionofsouls.model.unit.DamageResult
import net.firstrateconcepts.fusionofsouls.model.unit.HitCheck
import net.firstrateconcepts.fusionofsouls.model.unit.HitResult
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorHook
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorScope
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInteraction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import kotlin.math.roundToInt

class UnitInteractionService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val randomizer: RandomizerService,
    private val engine: AsyncPooledEngine,
    private val unitManager: UnitManager
) : RunService(eventBus, registry) {
    private val logger = fosLogger()

    fun canEntityAttack(entity: Entity): Boolean {
        if (!entity.hasTarget) return false

        return entity.isInRangeOfTarget
    }

    fun processEntityAttack(attacker: Entity) {
        if (!canEntityAttack(attacker)) return

        engine.withUnit(attacker.target!!) { defender ->
            runOnServiceThread {
                val rawRoll = randomizer.attackRoll()
                val attackBonus = attacker.attrs.attackBonus()
                doAttack(attacker, defender, rawRoll, attackBonus)
            }
        }
    }

    fun doAttack(attacker: Entity, defender: Entity, rawRoll: Int, attackBonus: Float) {
        val tag = "[${attacker.name} -> ${defender.name}]: "
        val combatStr = StringBuilder(tag)

        val evasion = defender.attrs.evasion()
        val hitResult = hitCheck(InterceptorScope.ATTACK, attacker, defender, HitCheck(rawRoll, attackBonus, evasion))

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

        val damage = damage(InterceptorScope.ATTACK, attacker, defender, DamageRequest(hitResult))

        combatStr.append("Damage Data: [Damage Scale: ${hitResult.damageScale} | Raw Damage: ${damage.rawDamage} | Defense: ${defender.attrs.defense} |")
            .append(" Final: ${damage.finalDamage} | Defender HP: ${defender.currentHp} / ${defender.attrs.maxHp()}]")
        logger.info { combatStr.toString() }
    }

    fun hitCheck(scope: InterceptorScope, unit: Entity, target: Entity, request: HitCheck): HitResult {
        intercept(scope, InterceptorHook.HIT_CHECK, unit, target, request)

        val finalRoll = request.rawRoll + request.attackBonus - request.evasion
        val expectedRoll = 50 + request.attackBonus - request.evasion
        val damageScale = 1 + ((finalRoll - expectedRoll) / 200)
        val result = HitResult(request.rawRoll, finalRoll.roundToInt(), damageScale)

        val hitHook = if (result.isHit) InterceptorHook.ON_HIT else InterceptorHook.ON_MISS
        intercept(scope, hitHook, unit, target, result)
        if (result.isCrit) intercept(scope, InterceptorHook.ON_CRIT, unit, target, result)

        return result
    }

    fun damage(scope: InterceptorScope, unit: Entity, target: Entity, request: DamageRequest): DamageResult {
        intercept(scope, InterceptorHook.DAMAGE_CALC, unit, target, request)
        val critMulti = unit.attrs.critBonus()
        var damageMulti = request.hit.damageScale * request.additionalDamageMultiplier
        if (request.hit.isCrit) damageMulti *= critMulti

        val baseDamage = unit.attrs.baseDamage() + request.additionalBaseDamage
        val defense = target.attrs.defense()
        val rawDamage = baseDamage * damageMulti
        val finalDamage = (rawDamage / defense).roundToInt()

        unitManager.updateUnitHp(target.id, -finalDamage)
        val result = DamageResult(rawDamage.roundToInt(), finalDamage)
        intercept(scope, InterceptorHook.AFTER_DAMAGE, unit, target, result)
        return result
    }

    fun <I: UnitInteraction> intercept(scope: InterceptorScope, hook: InterceptorHook, unit: Entity, target: Entity, interaction: I) {
        unit.interceptAsUnit(scope, hook, target, interaction)
        target.interceptAsTarget(scope, hook, unit, interaction)
    }
}
