package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.component.TargetComponent
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.targetInfo
import net.firstrateconcepts.fusionofsouls.model.component.unitInfo
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger

val targetFamily = oneOf(TargetComponent::class).get()!!

// TODO: Find out for sure on the performance of this. It might be a good idea to make this an interval system and only update a couple of times per second
class TargetingSystem(engine: AsyncPooledEngine) : IteratingSystem(targetFamily) {
    private val logger = fosLogger()

    init {
        engine.addSystem(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val targetInfo = entity.targetInfo
        if (!targetInfo.canChangeTarget) return

        val unitTeam = entity.unitInfo.team
        val closestTarget = entities
            .filter { it.unitInfo.team != unitTeam && it.targetInfo.isTargetable }
            .minByOrNull { it.currentPosition.dst(entity.currentPosition) }?.id
            ?: targetInfo.target

        if (closestTarget != targetInfo.target) {
            logger.info { "Unit(${entity.id} | ${entity.name}} changing target to [$closestTarget]" }
            targetInfo.target = closestTarget
        }
    }
}
