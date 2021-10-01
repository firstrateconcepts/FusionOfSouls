package net.firstrateconcepts.fusionofsouls.service

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import ktx.ashley.oneOf
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.service.asset.UnitAssets
import net.firstrateconcepts.fusionofsouls.service.entity.UnitBuilder
import net.firstrateconcepts.fusionofsouls.util.ext.entityToEventListener
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunInitializer(
    private val engine: PooledEngine,
    private val unitBuilder: UnitBuilder,
    private val unitAssets: UnitAssets,
    eventBus: EventBus
) : Disposable {
    private val activatedFamily = oneOf(ActiveComponent::class).get()
    private val activeListener = eventBus.entityToEventListener({e -> UnitActivatedEvent(e.id)}, {e -> UnitDeactivatedEvent(e.id)})

    fun initialize() {
        engine.addEntityListener(activatedFamily, activeListener)
        unitBuilder.buildUnit("Hero", unitAssets.heroAsset) {
            with<ActiveComponent>()
            entity.add(PositionComponent(Vector2(0f, 0f)))
        }
    }

    override fun dispose() {
        engine.removeAllEntities()
        engine.removeEntityListener(activeListener)
    }
}
