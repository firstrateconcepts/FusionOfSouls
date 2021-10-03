package net.firstrateconcepts.fusionofsouls.service.duringRun

import net.firstrateconcepts.fusionofsouls.model.BattleStatus
import net.firstrateconcepts.fusionofsouls.model.event.RunStatusChanged
import net.firstrateconcepts.fusionofsouls.service.entity.EnemyGenerator
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent

class BattleManager(private val enemyGenerator: EnemyGenerator) : RunService() {
    private val logger = fosLogger()

    @HandlesEvent
    fun statusChanged(event: RunStatusChanged) = runOnServiceThread {
        when(event.newStatus) {
            BattleStatus.BEFORE_BATTLE -> newBattle()
            BattleStatus.AFTER_BATTLE -> battleComplete()
            else -> Unit
        }
    }

    fun newBattle() {
        logger.info { "Building new battle" }
        enemyGenerator.generateEnemies()
    }

    fun battleComplete() {
        logger.info { "Battle complete" }
    }
}
