package net.firstrateconcepts.fusionofsouls.model.loot.passive

import net.firstrateconcepts.fusionofsouls.model.unit.UnitInteraction
import net.firstrateconcepts.fusionofsouls.model.unit.UnitInterceptor

interface PassiveStrategy

enum class InterceptorTarget {
    UNIT, TARGET, BOTH;

    val isUnit get() = this == UNIT || this == BOTH
    val isTarget get() = this == TARGET || this == BOTH
}

class InterceptorPassiveStrategy(vararg val interceptors: Pair<InterceptorTarget, UnitInterceptor<out UnitInteraction>>) : PassiveStrategy
