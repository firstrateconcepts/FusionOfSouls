package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.model.event.newBattle
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.HeroService
import net.firstrateconcepts.fusionofsouls.service.unit.UnitManager
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunInitializer(
    private val engine: AsyncPooledEngine,
    private val runServiceRegistry: RunServiceRegistry,
    private val eventBus: EventBus,
    private val heroService: HeroService
) : Disposable {
    fun initialize() {
        runServiceRegistry.startAll()
        heroService.initializeHero()
        eventBus.newBattle()
    }

    override fun dispose() {
        runServiceRegistry.stopAll()
        engine.removeAllEntities()
    }
}
