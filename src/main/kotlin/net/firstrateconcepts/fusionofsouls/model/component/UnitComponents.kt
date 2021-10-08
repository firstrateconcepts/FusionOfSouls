package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType

class UnitComponent(val type: UnitType, val team: UnitTeam) : Component
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!
val Entity.team get() = unitInfo.team

class TargetComponent : Component {
    var target: Int? = null
    var canChangeTarget = true
    var isTargetable = true
}
val targetMapper = mapperFor<TargetComponent>()
val Entity.targetInfo get() = this[targetMapper]!!
val Entity.currentTarget get() = targetInfo.target

class AttackingComponent(var attackTimer: Float = 0f, var attackTimerReady: Float = 0f) : Component
val attackMapper = mapperFor<AttackingComponent>()
val Entity.attackInfo get() = this[attackMapper]
val Entity.isAttacking get() = attackInfo != null
