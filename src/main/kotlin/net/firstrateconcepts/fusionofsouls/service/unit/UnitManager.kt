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
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.NameComponent
import net.firstrateconcepts.fusionofsouls.model.component.SteerableComponent
import net.firstrateconcepts.fusionofsouls.model.component.TextureComponent
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.lifesteal
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.unit.ActionsComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.BattleDataComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.ClassesComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.EffectsComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.InterceptorsComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.PassivesComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.TargetComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.TimersComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.currentHp
import net.firstrateconcepts.fusionofsouls.model.component.unit.team
import net.firstrateconcepts.fusionofsouls.model.component.unit.unitFamily
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.HpChangedEvent
import net.firstrateconcepts.fusionofsouls.model.event.KillUnitEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.model.loot.passive.PassiveDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.unitClass.UnitClass
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.with
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import kotlin.math.roundToInt

class UnitManager(
    private val engine: AsyncPooledEngine,
    private val assets: AssetStorage,
    private val eventBus: EventBus,
    registry: RunServiceRegistry,
    private val attributeService: AttributeService,
    private val passiveService: PassiveService
) : RunService(eventBus, registry) {
    private val logger = fosLogger()

    // TODO: Better unit creation, remove suppression
    @SuppressWarnings("LongParameterList")
    fun buildUnit(
        name: String,
        texture: UnitTexture,
        ability: AbilityDefinition,
        passive: PassiveDefinition,
        type: UnitType,
        team: UnitTeam,
        classes: List<UnitClass>,
        config: EngineEntity.() -> Unit = {}
    ): Entity {
        val unit = engine.entity {
            with<IdComponent>()
            with<UnitComponent>(type, team, ability)
            with<PassivesComponent>()
            with<InterceptorsComponent>()
            with<NameComponent>(name)
            with<TextureComponent>(assets.get<Texture>(texture.assetFile))
            with<AttributesComponent>()
            with<AttributeModifiersComponent>()
            with<ClassesComponent>(classes.associateWith { 1 }.toMutableMap())
            config()
        }

        attributeService.recalculateAll(unit)
        passiveService.addPassive(unit, passive)
        return unit
    }

    fun activateUnit(id: Int, initialPosition: Vector2) = engine.withUnit(id) {
        engine.configureEntity(it) {
            with<ActiveComponent>()
            with<SteerableComponent>(initialPosition, if (entity.team == UnitTeam.PLAYER) 270f else 90f)
            with<TimersComponent>()
            with<ActionsComponent>()
            with<BattleDataComponent>(entity.attrs.maxHp())
            with<AliveComponent>()
            with<EffectsComponent>()
        }

        eventBus.enqueueEvent(UnitActivatedEvent(id))
    }

    fun deactivateUnit(id: Int) = engine.withUnit(id) {
        it.remove<ActiveComponent>()
        it.remove<TargetComponent>()
        it.remove<SteerableComponent>()

        eventBus.enqueueEvent(UnitDeactivatedEvent(id))
    }

    fun updateUnitHp(entity: Entity, hpDiff: Int) {
        if (hpDiff == 0) return
        entity.currentHp += hpDiff
        if (entity.currentHp <= 0) killUnit(entity)
        eventBus.enqueueEventSync(HpChangedEvent(entity.id, entity.currentHp, entity.attrs.maxHp()))
    }

    fun lifesteal(unit: Entity, finalDamage: Int, isAbility: Boolean) {
        val lifestealAmount = finalDamage * (unit.attrs.lifesteal() / 100f) * (if (isAbility) 0.25f else 1f)
        updateUnitHp(unit, lifestealAmount.roundToInt())
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
