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

interface UnitInterceptor<I : UnitInteraction> {
    val hook: InterceptorHook
    fun intercept(unit: Entity, target: Entity, interaction: I)
}

fun hitCheck(intercept: (Entity, Entity, HitCheck) -> Unit) = object : UnitInterceptor<HitCheck> {
    override val hook = InterceptorHook.HIT_CHECK
    override fun intercept(unit: Entity, target: Entity, interaction: HitCheck) = intercept(unit, target, interaction)
}

fun onHit(intercept: (Entity, Entity, HitResult) -> Unit) = object : UnitInterceptor<HitResult> {
    override val hook = InterceptorHook.ON_HIT
    override fun intercept(unit: Entity, target: Entity, interaction: HitResult) = intercept(unit, target, interaction)
}

fun onMiss(intercept: (Entity, Entity, HitResult) -> Unit) = object : UnitInterceptor<HitResult> {
    override val hook = InterceptorHook.ON_MISS
    override fun intercept(unit: Entity, target: Entity, interaction: HitResult) = intercept(unit, target, interaction)
}

fun onCrit(intercept: (Entity, Entity, HitResult) -> Unit) = object : UnitInterceptor<HitResult> {
    override val hook = InterceptorHook.ON_CRIT
    override fun intercept(unit: Entity, target: Entity, interaction: HitResult) = intercept(unit, target, interaction)
}

fun beforeDamage(intercept: (Entity, Entity, DamageRequest) -> Unit) = object : UnitInterceptor<DamageRequest> {
    override val hook = InterceptorHook.DAMAGE_CALC
    override fun intercept(unit: Entity, target: Entity, interaction: DamageRequest) = intercept(unit, target, interaction)
}

fun afterDamage(intercept: (Entity, Entity, DamageResult) -> Unit) = object : UnitInterceptor<DamageResult> {
    override val hook = InterceptorHook.AFTER_DAMAGE
    override fun intercept(unit: Entity, target: Entity, interaction: DamageResult) = intercept(unit, target, interaction)
}
