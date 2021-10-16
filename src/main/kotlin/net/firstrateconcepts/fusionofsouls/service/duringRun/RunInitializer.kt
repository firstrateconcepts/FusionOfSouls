package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.newBattle
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.hero.DefaultHeroDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.hero.getHeroDefinitionForId
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.UnitManager
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

        val entity = unitManager.buildUnit(hero.name, hero.texture, hero.ability, hero.passive, UnitType.HERO, UnitTeam.PLAYER) {
            // TODO: This is not real, please remember to remove this
//            entity.attrMods.add(AttributeModifier(AttributeType.BASE_DAMAGE, percentModifier = 200f))
        }
        unitManager.activateUnit(entity.id, Vector2())

        eventBus.newBattle()
    }

    override fun dispose() {
        runServiceRegistry.stopAll()
        engine.removeAllEntities()
    }
}
