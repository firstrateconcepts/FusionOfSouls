package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.PositionComponent
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.AttributeRecalculateNeededEvent
import net.firstrateconcepts.fusionofsouls.service.asset.UnitAssets
import net.firstrateconcepts.fusionofsouls.service.entity.UnitBuilder
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class RunInitializer(
    private val engine: PooledEngine,
    private val unitBuilder: UnitBuilder,
    private val unitAssets: UnitAssets,
    private val runServiceRegistry: RunServiceRegistry,
    private val eventBus: EventBus
) : Disposable {
    fun initialize() {
        runServiceRegistry.startAll()

        val unit = unitBuilder.buildUnit("Hero", unitAssets.heroAsset) {
            with<ActiveComponent>()
            entity.add(PositionComponent(Vector2(0f, 0f)))
        }

        eventBus.enqueueEventSync(AttributeRecalculateNeededEvent(unit.id))
    }

    override fun dispose() {
        runServiceRegistry.stopAll()
        engine.removeAllEntities()
    }
}
