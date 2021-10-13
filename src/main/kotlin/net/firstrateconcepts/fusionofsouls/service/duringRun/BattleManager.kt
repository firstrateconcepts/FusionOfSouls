package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.component.aliveUnitFamily
import net.firstrateconcepts.fusionofsouls.model.component.team
import net.firstrateconcepts.fusionofsouls.model.event.NewBattleEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDiedEvent
import net.firstrateconcepts.fusionofsouls.model.event.battleComplete
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.EnemyGenerator
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class BattleManager(private val enemyGenerator: EnemyGenerator, override val eventBus: EventBus, private val engine: AsyncPooledEngine) : RunService() {
    private val logger = fosLogger()

    @HandlesEvent(NewBattleEvent::class)
    fun newBattle() {
        logger.info { "Building new battle" }
        enemyGenerator.generateEnemies()
    }

    @HandlesEvent
    fun unitDied(event: UnitDiedEvent) {
        engine.withUnit(event.unitId) { entity ->
            runOnServiceThread {
                if (engine.getEntitiesFor(aliveUnitFamily).none { it.team == entity.team }) {
                    battleComplete()
                }
            }
        }
    }

    private fun battleComplete() {
        logger.info { "Battle complete" }
        eventBus.battleComplete()
    }
}
