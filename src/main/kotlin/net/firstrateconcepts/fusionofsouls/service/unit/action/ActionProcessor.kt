package net.firstrateconcepts.fusionofsouls.service.unit.action

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.model.unit.action.UnitAction
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.findById

abstract class ActionProcessor<A : UnitAction> {
    private val engine = inject<AsyncPooledEngine>()

    fun process(action: A) {
        engine.findById(action.unitId)?.run { processInternal(this, action) }
    }

    protected abstract fun processInternal(entity: Entity, action: A)
}

inline fun <reified A : UnitAction> actionProcessor(crossinline handler: (Entity, A) -> Unit) = object : ActionProcessor<A>() {
    override fun processInternal(entity: Entity, action: A) = handler(entity, action)
}
