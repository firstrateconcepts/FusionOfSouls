package net.firstrateconcepts.fusionofsouls.service.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import net.firstrateconcepts.fusionofsouls.model.component.unit.AliveComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.EffectsComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.component.unit.effects
import net.firstrateconcepts.fusionofsouls.model.component.unit.timers
import net.firstrateconcepts.fusionofsouls.model.unit.effect.Effect
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.unit.EffectService

val effectsFamily = allOf(UnitComponent::class, AliveComponent::class, EffectsComponent::class).get()!!

class EffectsSystem(engine: AsyncPooledEngine, private val effectService: EffectService) : IteratingSystem(effectsFamily) {
    init {
        engine.addSystem(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val effectsToExpire = mutableListOf<Effect>()

        entity.effects.forEach { effect ->
            entity.timers[effect.timerId]?.apply {
                if (isReady) {
                    effectsToExpire.add(effect)
                }
            }
        }

        effectsToExpire.forEach { effect ->
            entity.timers.remove(effect.timerId)
            effectService.removeEffect(entity, effect)
        }
    }
}
