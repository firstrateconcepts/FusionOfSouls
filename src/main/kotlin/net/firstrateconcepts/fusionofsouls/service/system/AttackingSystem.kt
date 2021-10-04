package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.AttackingComponent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine

val attackingFamily = oneOf(AttackingComponent::class).get()

class AttackingSystem(engine: AsyncPooledEngine) : IteratingSystem(attackingFamily) {
    init {
        engine.addSystem(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) = Unit
}
