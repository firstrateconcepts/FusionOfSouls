package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.EngineEntity
import ktx.reflect.reflect
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.idFamily
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine

fun AsyncPooledEngine.withUnit(id: Int, callback: suspend (Entity) -> Unit) =
    runOnEngineThread { getEntitiesFor(idFamily).find { it.id == id }?.apply { callback(this) } }

fun AsyncPooledEngine.withHero(callback: suspend (Entity) -> Unit) = withUnit(0, callback)

inline fun <reified T : Component> EngineEntity.with(vararg params: Any): T {
    val constructor = reflect<T>().constructor
    val component = constructor.newInstance(*params) as T
    entity.add(component)
    return component
}
