package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.changeRunStatus
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.hero.DefaultHeroDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.hero.getHeroDefinitionForId
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.entity.UnitManager
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunInitializer(
    private val engine: AsyncPooledEngine,
    private val unitManager: UnitManager,
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

        unitManager.buildUnit(hero.name, hero.texture, UnitType.HERO, UnitTeam.PLAYER) {
            unitManager.activateUnit(entity.id, Vector2())
        }

        eventBus.changeRunStatus(BattleStatus.BEFORE_BATTLE)
    }

    override fun dispose() {
        runServiceRegistry.stopAll()
        engine.removeAllEntities()
    }
}
