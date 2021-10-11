package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.component.steerable
import net.firstrateconcepts.fusionofsouls.model.event.BattleCompletedEvent
import net.firstrateconcepts.fusionofsouls.model.event.BattleStartedEvent
import net.firstrateconcepts.fusionofsouls.model.event.NewBattleEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.system.steeringFamily
import net.firstrateconcepts.fusionofsouls.service.unit.EnemyGenerator
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class BattleManager(private val enemyGenerator: EnemyGenerator, override val eventBus: EventBus, private val engine: AsyncPooledEngine) : RunService() {
    private val logger = fosLogger()

    @HandlesEvent(NewBattleEvent::class)
    fun newBattle() {
        logger.info { "Building new battle" }
        enemyGenerator.generateEnemies()
    }

    @HandlesEvent(BattleStartedEvent::class)
    fun battleStarted() {
        logger.info { "Starting battle" }
        val steerables = engine.getEntitiesFor(steeringFamily).map { it.steerable }
        steerables.forEach { steer ->
            val notMe = steerables.filter { it != steer }
            steer.steeringBehavior.proximityAgents.apply {
                clear()
                addAll(notMe)
            }
        }
    }

    @HandlesEvent(BattleCompletedEvent::class)
    fun battleComplete() {
        logger.info { "Battle complete" }
    }
}
