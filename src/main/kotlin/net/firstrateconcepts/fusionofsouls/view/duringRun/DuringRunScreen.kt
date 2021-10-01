package net.firstrateconcepts.fusionofsouls.view.duringRun

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.service.engine.MovementSystem
import net.firstrateconcepts.fusionofsouls.service.entity.UnitBuilder
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.GameScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController

class DuringRunScreen(
    override val gameController: DuringRunGameController,
    override val uiController: DuringRunUiController,
    private val unitBuilder: UnitBuilder,
    private val assetStorage: AssetStorage,
    private val pooledEngine: PooledEngine,
    private val movementSystem: MovementSystem,
    private val eventBus: EventBus
) : GameScreen(16f, 9f) {
    override fun show() {
        pooledEngine.addSystem(movementSystem)
        eventBus.registerHandler(gameController)
        val unit = unitBuilder.buildUnit("Hero", assetStorage["unit/heroArrow-tp.png"], Vector2(0f, 0f))
        gameController.addNewUnit(unit)
        super.show()
    }

    override fun hide() {
        pooledEngine.removeAllSystems()
        pooledEngine.removeAllEntities()
        eventBus.deregisterHandler(gameController)
    }

    override fun render(delta: Float) {
        pooledEngine.update(delta)
        super.render(delta)
    }
}
