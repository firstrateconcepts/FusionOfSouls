package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import ktx.ashley.EngineEntity
import ktx.ashley.configureEntity
import ktx.ashley.entity
import ktx.ashley.remove
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.model.component.ActionsComponent
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.BattleDataComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.NameComponent
import net.firstrateconcepts.fusionofsouls.model.component.SteerableComponent
import net.firstrateconcepts.fusionofsouls.model.component.TargetComponent
import net.firstrateconcepts.fusionofsouls.model.component.TextureComponent
import net.firstrateconcepts.fusionofsouls.model.component.TimersComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.currentHp
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.team
import net.firstrateconcepts.fusionofsouls.model.component.unitFamily
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.HpChangedEvent
import net.firstrateconcepts.fusionofsouls.model.event.KillUnitEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.with
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class UnitManager(
    private val engine: AsyncPooledEngine,
    private val assets: AssetStorage,
    override val eventBus: EventBus,
    private val attributeCalculator: AttributeCalculator
) : RunService() {
    private val logger = fosLogger()

    fun buildUnit(
        name: String,
        texture: UnitTexture,
        type: UnitType,
        team: UnitTeam,
        config: EngineEntity.() -> Unit = {}
    ): Entity {
        val unit = engine.entity {
            with<IdComponent>()
            with<UnitComponent>(type, team)
            with<NameComponent>(name)
            with<TextureComponent>(assets.get<Texture>(texture.assetFile))
            with<AttributesComponent>()
            with<AttributeModifiersComponent>()
            config()
        }

        attributeCalculator.recalculate(unit)
        return unit
    }

    fun activateUnit(id: Int, initialPosition: Vector2) = engine.runOnEngineThread {
        engine.withUnit(id) {
            engine.configureEntity(it) {
                with<ActiveComponent>()
                with<SteerableComponent>(initialPosition, if (entity.team == UnitTeam.PLAYER) 270f else 90f)
                with<TimersComponent>()
                with<ActionsComponent>()
                with<BattleDataComponent>(entity.attrs.maxHp())
                with<AliveComponent>()
            }

            eventBus.enqueueEvent(UnitActivatedEvent(id))
        }
    }

    fun deactivateUnit(id: Int) = engine.runOnEngineThread {
        engine.withUnit(id) {
            it.remove<ActiveComponent>()
            it.remove<TargetComponent>()
            it.remove<SteerableComponent>()

            eventBus.enqueueEvent(UnitDeactivatedEvent(id))
        }
    }

    fun updateUnitHp(unitId: Int, hpDiff: Float) {
        engine.withUnit(unitId) { entity ->
            entity.currentHp += hpDiff
            if (entity.currentHp <= 0) killUnit(entity)
            eventBus.enqueueEvent(HpChangedEvent(unitId, entity.currentHp, entity.attrs.maxHp()))
        }
    }

    private fun killUnit(entity: Entity) {
        entity.remove<AliveComponent>()
        eventBus.enqueueEventSync(KillUnitEvent(entity.id))
    }

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleEnded() = runOnServiceThread {
        logger.info { "Battle ended, refreshing battle-related components" }
        engine.getEntitiesFor(unitFamily).forEach {
            engine.configureEntity(it) {
                with<ActionsComponent>()
                with<BattleDataComponent>(entity.attrs.maxHp())
                eventBus.enqueueEvent(HpChangedEvent(entity.id, entity.currentHp, entity.attrs.maxHp()))
            }
        }
    }
}
