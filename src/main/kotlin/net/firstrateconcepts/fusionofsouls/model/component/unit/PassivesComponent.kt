package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import ktx.ashley.get
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition

class PassivesComponent : Component {
    val passives = mutableListOf<PassiveDefinition>()
}

val passivesMapper = mapperFor<PassivesComponent>()
val Entity.passives get() = this[passivesMapper]!!.passives
