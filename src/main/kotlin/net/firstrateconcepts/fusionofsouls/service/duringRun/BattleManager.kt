package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.component.steerable
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.entity.EnemyGenerator
import net.firstrateconcepts.fusionofsouls.service.system.steeringFamily
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class BattleManager(private val enemyGenerator: EnemyGenerator, override val eventBus: EventBus, private val engine: AsyncPooledEngine) : RunService() {
    private val logger = fosLogger()

    @HandlesEvent
    fun statusChanged(event: RunStatusChanged) = runOnServiceThread {
        when(event.newStatus) {
            BattleStatus.BEFORE_BATTLE -> newBattle()
            BattleStatus.AFTER_BATTLE -> battleComplete()
            BattleStatus.DURING_BATTLE -> battleStarted()
        }
    }

    private fun newBattle() {
        logger.info { "Building new battle" }
        enemyGenerator.generateEnemies()
    }

    private fun battleStarted() {
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

    private fun battleComplete() {
        logger.info { "Battle complete" }
    }
}
