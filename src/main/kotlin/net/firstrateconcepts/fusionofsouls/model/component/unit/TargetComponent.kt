package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import ktx.ashley.get

class TargetComponent(var target: Int) : Component

val targetMapper = mapperFor<TargetComponent>()
val Entity.target get() = this[targetMapper]?.target
val Entity.hasTarget get() = target != null
