package net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.actors.then
import ktx.async.onRenderingThread
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.rotation
import net.firstrateconcepts.fusionofsouls.model.event.UnitAttackingEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.system.SteeringSystem
import net.firstrateconcepts.fusionofsouls.util.ext.degRad
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.HandlesEvent
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent
import kotlin.math.cos
import kotlin.math.sin

@Scene2dDsl
fun <S> KWidget<S>.unit(unit: UnitViewModel, init: UnitView.(S) -> Unit = {}) = uiComponent<S, UnitController, UnitView>({
    this.vm = unit
}, init)

class UnitController(private val engine: AsyncPooledEngine, private val eventBus: EventBus, private val steeringSystem: SteeringSystem) : Controller {
    private val logger = fosLogger()
    override lateinit var vm: UnitViewModel
    override val view by lazy { UnitView(this, vm) }
    val unitId by lazy { vm.id }

    override fun load() {
        eventBus.registerHandlers(this)
        steeringSystem.onUnitMove(unitId) {
            vm.position(currentPosition.cpy())
            vm.rotation(rotation)
        }
    }

    @HandlesEvent
    suspend fun unitAttacking(event: UnitAttackingEvent) {
        if (!filterEvent(event)) return

        onRenderingThread {
            logger.info { "Performing unit attack animation for [${event.unitId}]" }
            withUnit {
                val xMove = cos((rotation + 90f).degRad) * 0.1f
                val yMove = sin((rotation + 90f).degRad) * 0.1f
                val action = Actions.moveBy(xMove, yMove, 0.025f) then Actions.moveTo(currentPosition.x, currentPosition.y, 0.15f)
                vm.actorActions(mutableListOf(action))
            }
        }
    }

    override fun dispose() {
        logger.info { "Disposing unit [$unitId]" }
        eventBus.unregisterHandlers(this)
        steeringSystem.removeUnitMoveCallback(unitId)
        super.dispose()
    }

    private fun filterEvent(event: UnitEvent) = event.unitId == vm.id
    private fun withUnit(callback: suspend Entity.() -> Unit) = engine.withUnit(unitId) { onRenderingThread { callback() } }
}
