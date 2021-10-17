package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.ashley.get

class AliveComponent : Component

val aliveMapper = mapperFor<AliveComponent>()
val Entity.alive get() = this[aliveMapper]
val Entity.isAlive get() = alive != null
val aliveUnitFamily = allOf(UnitComponent::class, AliveComponent::class).get()!!
