package net.firstrateconcepts.fusionofsouls.model.unit

import com.badlogic.ashley.core.Entity

enum class InterceptorHook {
    HIT_CHECK,
    ON_HIT,
    ON_MISS,
    ON_CRIT,
    DAMAGE_CALC,
    AFTER_DAMAGE
}

enum class InterceptorScope {
    ATTACK, ABILITY, BOTH;

    fun matches(scope: InterceptorScope) = this == BOTH || this == scope
}

interface UnitInterceptor<I : UnitInteraction> {
    val hook: InterceptorHook
    val scope: InterceptorScope
    fun intercept(unit: Entity, target: Entity, interaction: I)
}

fun <I : UnitInteraction> intercept(scope: InterceptorScope, hook: InterceptorHook, intercept: (Entity, Entity, I) -> Unit) = object : UnitInterceptor<I> {
    override val hook = hook
    override val scope = scope

    override fun intercept(unit: Entity, target: Entity, interaction: I) = intercept(unit, target, interaction)
}

fun hitCheck(scope: InterceptorScope, intercept: (Entity, Entity, HitCheck) -> Unit) = intercept(scope, InterceptorHook.HIT_CHECK, intercept)
fun onHit(scope: InterceptorScope, intercept: (Entity, Entity, HitResult) -> Unit) = intercept(scope, InterceptorHook.ON_HIT, intercept)
fun onMiss(scope: InterceptorScope, intercept: (Entity, Entity, HitResult) -> Unit) = intercept(scope, InterceptorHook.ON_MISS, intercept)
fun onCrit(scope: InterceptorScope, intercept: (Entity, Entity, HitResult) -> Unit) = intercept(scope, InterceptorHook.ON_CRIT, intercept)
fun beforeDamage(scope: InterceptorScope, intercept: (Entity, Entity, DamageRequest) -> Unit) = intercept(scope, InterceptorHook.DAMAGE_CALC, intercept)
fun afterDamage(scope: InterceptorScope, intercept: (Entity, Entity, DamageResult) -> Unit) = intercept(scope, InterceptorHook.AFTER_DAMAGE, intercept)
