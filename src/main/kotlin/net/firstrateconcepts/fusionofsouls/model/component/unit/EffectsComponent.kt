package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import ktx.ashley.get
import net.firstrateconcepts.fusionofsouls.model.unit.effect.Effect

class EffectsComponent : Component {
    val effects = mutableListOf<Effect>()
}

val effectsMapper = mapperFor<EffectsComponent>()
val Entity.effects get() = this[effectsMapper]!!.effects
