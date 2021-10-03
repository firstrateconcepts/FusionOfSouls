package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import java.util.concurrent.atomic.AtomicInteger

private var idSequence = AtomicInteger(0)

class IdComponent(val id: Int = idSequence.getAndIncrement()) : Component
val idMapper = mapperFor<IdComponent>()
val Entity.id get() = this[idMapper]!!.id
val idFamily = oneOf(IdComponent::class).get()!!

class NameComponent(val name: String) : Component
val nameMapper = mapperFor<NameComponent>()
val Entity.name get() = this[nameMapper]!!.name

class TextureComponent(val texture: Texture) : Component
val textureMapper = mapperFor<TextureComponent>()
val Entity.texture get() = this[textureMapper]!!.texture

class ActiveComponent : Component
