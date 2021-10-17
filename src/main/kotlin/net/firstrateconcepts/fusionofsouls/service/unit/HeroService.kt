package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.unit.RunesComponent
import net.firstrateconcepts.fusionofsouls.model.loot.Rarity
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.hero.DefaultHeroDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.hero.getHeroDefinitionForId
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class HeroService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val runStateService: RunStateService,
    private val engine: AsyncPooledEngine,
    private val unitManager: UnitManager,
    private val runeService: RuneService
) : RunService(eventBus, registry) {
    private fun withHero(callback: suspend (Entity) -> Unit) = engine.withUnit(runStateService.load().selectedHeroId, callback)

    fun initializeHero() {
        val runState = runStateService.load()
        val hero = getHeroDefinitionForId(runState.selectedHeroId) ?: DefaultHeroDefinition

        runStateService.save(runState.apply {
            unitCap = hero.unitCap
            runeCap = hero.runeCap
            fusionCap = hero.fusionCap
        })

        val entity = unitManager.buildUnit(hero.name, hero.texture, hero.ability, hero.passive, UnitType.HERO, UnitTeam.PLAYER) {
            with<RunesComponent>()
            val rune = runeService.generateRune(Rarity.COMMON)
            runeService.addRune(this@buildUnit.entity, rune)
        }

        unitManager.activateUnit(entity.id, Vector2())
    }
}
