package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor

class XpComponent : Component {
    var xp = 3
    var xpToLevel = 10
    var level = 1
}
val xpMapper = mapperFor<XpComponent>()
val Entity.xp get() = this[xpMapper]!!
