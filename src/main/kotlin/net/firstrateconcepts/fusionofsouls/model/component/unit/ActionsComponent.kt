package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import kotlinx.coroutines.channels.Channel
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.action.ActionBlocker
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction

class ActionsComponent : Component {
    val queue = Channel<UnitAction>()
    val blockers = mutableListOf<ActionBlocker>()
}

val actionsMapper = mapperFor<ActionsComponent>()
val Entity.actions get() = this[actionsMapper]!!
