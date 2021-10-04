package net.firstrateconcepts.fusionofsouls.service.entity

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import ktx.ashley.EngineEntity
import ktx.ashley.configureEntity
import ktx.ashley.entity
import ktx.ashley.remove
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.NameComponent
import net.firstrateconcepts.fusionofsouls.model.component.SteerableComponent
import net.firstrateconcepts.fusionofsouls.model.component.TargetComponent
import net.firstrateconcepts.fusionofsouls.model.component.TextureComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.team
import net.firstrateconcepts.fusionofsouls.model.event.AttributeRecalculateNeededEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.findById
import net.firstrateconcepts.fusionofsouls.util.ext.with
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class UnitManager(private val engine: AsyncPooledEngine, private val assets: AssetStorage, private val eventBus: EventBus) {
    fun buildUnit(
        name: String,
        texture: UnitTexture,
        type: UnitType,
        team: UnitTeam,
        config: EngineEntity.() -> Unit = {}
    ) = engine.runOnEngineThread {
        val unit = engine.entity {
            with<IdComponent>()
            with<UnitComponent>(type, team)
            with<NameComponent>(name)
            with<TextureComponent>(assets.get<Texture>(texture.assetFile))
            with<AttributesComponent>()
            with<AttributeModifiersComponent>()
            config()
        }

        eventBus.enqueueEvent(AttributeRecalculateNeededEvent(unit.id))
    }

    fun activateUnit(id: Int, initialPosition: Vector2) = engine.runOnEngineThread {
        engine.findById(id)?.apply {
            engine.configureEntity(this) {
                with<ActiveComponent>()
                with<TargetComponent>()
                with<SteerableComponent>(initialPosition, if (entity.team == UnitTeam.PLAYER) 270f else 90f)
            }

            eventBus.enqueueEvent(UnitActivatedEvent(id))
        }
    }

    fun deactivateUnit(id: Int) = engine.runOnEngineThread {
        engine.findById(id)?.apply {
            remove<ActiveComponent>()
            remove<TargetComponent>()
            remove<SteerableComponent>()
        }

        eventBus.enqueueEvent(UnitDeactivatedEvent(id))
    }
}
