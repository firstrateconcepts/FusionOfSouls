package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import ktx.ashley.EngineEntity
import ktx.reflect.reflect
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.idFamily
import java.util.*

fun Engine.findById(id: UUID) = getEntitiesFor(idFamily).find { it.id == id }

inline fun <reified T : Component> EngineEntity.with(vararg params: Any): T {
    val constructor = reflect<T>().constructor
    val component = constructor.newInstance(*params) as T
    entity.add(component)
    return component
}
