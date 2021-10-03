package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.AttributeRecalculateNeededEvent
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.hero.DefaultHeroDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.hero.getHeroDefinitionForId
import net.firstrateconcepts.fusionofsouls.service.entity.UnitBuilder
import net.firstrateconcepts.fusionofsouls.util.ext.with
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunInitializer(
    private val engine: PooledEngine,
    private val unitBuilder: UnitBuilder,
    private val assets: AssetStorage,
    private val runServiceRegistry: RunServiceRegistry,
    private val eventBus: EventBus,
    private val runStateService: RunStateService
) : Disposable {
    fun initialize() {
        val runState = runStateService.load()
        val hero = getHeroDefinitionForId(runState.selectedHeroId) ?: DefaultHeroDefinition

        runStateService.save(runState.apply {
            unitCap = hero.unitCap
            runeCap = hero.runeCap
            fusionCap = hero.fusionCap
        })

        runServiceRegistry.startAll()

        val unit = unitBuilder.buildUnit(hero.name, assets[hero.texture.assetFile], UnitType.HERO, UnitTeam.PLAYER) {
            with<ActiveComponent>()
            with<PositionComponent>(Vector2(0f, 0f))
        }

        eventBus.enqueueEventSync(AttributeRecalculateNeededEvent(unit.id))
    }

    override fun dispose() {
        runServiceRegistry.stopAll()
        engine.removeAllEntities()
    }
}
