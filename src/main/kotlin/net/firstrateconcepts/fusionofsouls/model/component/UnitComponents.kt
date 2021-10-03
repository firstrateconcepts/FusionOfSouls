package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType

class PositionComponent(val position: Vector2) : Component
val positionMapper = mapperFor<PositionComponent>()
val Entity.position get() = this[positionMapper]!!.position

class UnitComponent(val type: UnitType, val team: UnitTeam) : Component
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!
