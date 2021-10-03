package net.firstrateconcepts.fusionofsouls.service.entity

import com.badlogic.gdx.graphics.Texture
import ktx.ashley.EngineEntity
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.NameComponent
import net.firstrateconcepts.fusionofsouls.model.component.TextureComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.with

class UnitBuilder(private val engine: AsyncPooledEngine, private val assets: AssetStorage) {
    fun buildUnit(name: String, texture: UnitTexture, type: UnitType, team: UnitTeam, config: EngineEntity.() -> Unit = {}) = engine.entity {
        with<IdComponent>()
        with<UnitComponent>(type, team)
        with<NameComponent>(name)
        with<TextureComponent>(assets.get<Texture>(texture.assetFile))
        with<AttributesComponent>()
        with<AttributeModifiersComponent>()
        config()
    }
}
