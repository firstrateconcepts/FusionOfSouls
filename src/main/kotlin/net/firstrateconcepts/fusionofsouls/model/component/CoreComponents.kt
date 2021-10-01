package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import java.util.*

class IdComponent(val id: UUID = UUID.randomUUID()) : Component
class NameComponent(val name: String) : Component
class TextureComponent(val texture: Texture) : Component
class PositionComponent(val position: Vector2) : Component
class UnitComponent : Component
