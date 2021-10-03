package net.firstrateconcepts.fusionofsouls.service.entity

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Texture
import ktx.ashley.EngineEntity
import ktx.ashley.entity
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.NameComponent
import net.firstrateconcepts.fusionofsouls.model.component.TextureComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.util.ext.with

class UnitBuilder(private val engine: PooledEngine) {
    fun buildUnit(name: String, texture: Texture, type: UnitType, team: UnitTeam, config: EngineEntity.() -> Unit = {}) = engine.entity {
        with<IdComponent>()
        with<UnitComponent>(type, team)
        with<NameComponent>(name)
        with<TextureComponent>(texture)
        with<AttributesComponent>()
        with<AttributeModifiersComponent>()
        config()
    }
}
