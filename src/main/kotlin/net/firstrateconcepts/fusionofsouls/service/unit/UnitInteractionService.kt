package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.baseDamage
import net.firstrateconcepts.fusionofsouls.model.component.critBonus
import net.firstrateconcepts.fusionofsouls.model.component.defense
import net.firstrateconcepts.fusionofsouls.model.component.unit.interceptAsTarget
import net.firstrateconcepts.fusionofsouls.model.component.unit.interceptAsUnit
import net.firstrateconcepts.fusionofsouls.model.unit.DamageRequest
import net.firstrateconcepts.fusionofsouls.model.unit.DamageResult
import net.firstrateconcepts.fusionofsouls.model.unit.HitCheck
import net.firstrateconcepts.fusionofsouls.model.unit.HitResult
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorHook
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorScope
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInteraction
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import kotlin.math.roundToInt

class UnitInteractionService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val unitManager: UnitManager
) : RunService(eventBus, registry) {

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

        unitManager.updateUnitHp(target, -finalDamage)
        unitManager.lifesteal(unit, finalDamage, scope == InterceptorScope.ABILITY)
        val result = DamageResult(rawDamage.roundToInt(), finalDamage)
        intercept(scope, InterceptorHook.AFTER_DAMAGE, unit, target, result)
        return result
    }

    private fun <I: UnitInteraction> intercept(scope: InterceptorScope, hook: InterceptorHook, unit: Entity, target: Entity, interaction: I) {
        unit.interceptAsUnit(scope, hook, target, interaction)
        target.interceptAsTarget(scope, hook, unit, interaction)
    }
}
