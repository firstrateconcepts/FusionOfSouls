package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType

class PositionComponent(val initialPosition: Vector2) : Component {
    val currentPosition = initialPosition.cpy()!!
}
val positionMapper = mapperFor<PositionComponent>()
val Entity.position get() = this[positionMapper]!!
val Entity.currentPosition get() = position.currentPosition

class UnitComponent(val type: UnitType, val team: UnitTeam) : Component
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!

class TargetComponent : Component {
    var target: Int? = null
    var canChangeTarget = true
    var isTargetable = true
}
val targetMapper = mapperFor<TargetComponent>()
val Entity.targetInfo get() = this[targetMapper]!!
val Entity.currentTarget get() = targetInfo.target
