package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor

class BattleDataComponent(maxHp: Float) : Component {
    var currentHp = maxHp
}

val battleDataMapper = mapperFor<BattleDataComponent>()
val Entity.battleData get() = this[battleDataMapper]!!
var Entity.currentHp
    get() = battleData.currentHp
    set(value) { battleData.currentHp = value }
