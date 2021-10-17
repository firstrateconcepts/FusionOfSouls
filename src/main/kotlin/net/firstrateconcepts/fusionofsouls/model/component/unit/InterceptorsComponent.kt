package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorHook
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorScope
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInteraction
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInterceptor

class InterceptorsComponent : Component {
    val asUnitInterceptors = mutableMapOf<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>()
    val asTargetInterceptors = mutableMapOf<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>()

    @Suppress("UNCHECKED_CAST")
    private fun MutableMap<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>.addInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) =
        computeIfAbsent(interceptor.hook) { mutableListOf() }.add(interceptor as UnitInterceptor<UnitInteraction>)

    fun addUnitInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asUnitInterceptors.addInterceptor(interceptor)
    fun addTargetInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asTargetInterceptors.addInterceptor(interceptor)
    fun removeUnitInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asUnitInterceptors[interceptor.hook]?.remove(interceptor)
    fun removeTargetInterceptor(interceptor: UnitInterceptor<out UnitInteraction>) = asTargetInterceptors[interceptor.hook]?.remove(interceptor)
}

val interceptorsMapper = mapperFor<InterceptorsComponent>()
val Entity.interceptors get() = this[interceptorsMapper]!!
private fun MutableMap<InterceptorHook, MutableList<UnitInterceptor<UnitInteraction>>>.intercept(
    scope: InterceptorScope,
    hook: InterceptorHook,
    unit: Entity,
    target: Entity,
    interaction: UnitInteraction
) = this[hook]?.filter { it.scope.matches(scope) }?.forEach { it.intercept(unit, target, interaction) }

fun Entity.interceptAsUnit(scope: InterceptorScope, hook: InterceptorHook, target: Entity, interaction: UnitInteraction) =
    interceptors.asUnitInterceptors.intercept(scope, hook, this, target, interaction)

fun Entity.interceptAsTarget(scope: InterceptorScope, hook: InterceptorHook, unit: Entity, interaction: UnitInteraction) =
    interceptors.asTargetInterceptors.intercept(scope, hook, unit, this, interaction)
