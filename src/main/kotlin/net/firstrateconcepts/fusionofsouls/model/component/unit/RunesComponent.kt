package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.loot.Rune

class RunesComponent : Component {
    val runes = mutableListOf<Rune>()
}

val runeMapper = mapperFor<RunesComponent>()
val Entity.runes get() = this[runeMapper]!!.runes
val Entity.activeRunes get() = runes.filter { it.active }
val Entity.inactiveRunes get() = runes.filter { !it.active }
