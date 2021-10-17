package net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.actors.then
import ktx.async.onRenderingThread
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.model.component.currentPosition
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.rotation
import net.firstrateconcepts.fusionofsouls.model.component.unit.ability
import net.firstrateconcepts.fusionofsouls.model.component.unit.abilityTimer
import net.firstrateconcepts.fusionofsouls.model.component.unit.attackTimer
import net.firstrateconcepts.fusionofsouls.model.event.HpChangedEvent
import net.firstrateconcepts.fusionofsouls.model.event.KillUnitEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitAbilityAnimationComplete
import net.firstrateconcepts.fusionofsouls.model.event.UnitAttackAnimationComplete
import net.firstrateconcepts.fusionofsouls.model.event.UnitAttackingEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDiedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitUsingAbilityEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.system.SteeringSystem
import net.firstrateconcepts.fusionofsouls.service.system.TimersSystem
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

// TODO: Runnable action is somehow getting reset somewhere sometimes that causes an NPE
class UnitController(
    private val engine: AsyncPooledEngine,
    private val eventBus: EventBus,
    private val steeringSystem: SteeringSystem,
    private val timersSystem: TimersSystem
) : Controller {
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

        timersSystem.onTimerTick(unitId) {
            vm.attackTimerPercent(attackTimer.percentComplete)
            vm.abilityTimerPercent(abilityTimer.percentComplete)
        }
    }

    @HandlesEvent
    fun hpChanged(event: HpChangedEvent) {
        if (!filterEvent(event)) return
        vm.hpPercent(event.hpPercent)
    }

    @HandlesEvent
    fun unitDied(event: KillUnitEvent) {
        if (!filterEvent(event)) return

        withUnit {
            val action = Actions.fadeOut(0.5f) then Actions.run { eventBus.enqueueEventSync(UnitDiedEvent(id)) } then Actions.removeActor()
            vm.addActorAction(ActorActionTarget.UNIT, action)
        }
    }

    @HandlesEvent
    suspend fun unitAttacking(event: UnitAttackingEvent) {
        if (!filterEvent(event)) return

        onRenderingThread {
            withUnit {
                val xMove = cos((rotation + 90f).degRad) * 0.1f
                val yMove = sin((rotation + 90f).degRad) * 0.1f
                val action = Actions.moveBy(xMove, yMove, 0.025f) then Actions.moveBy(-xMove, -yMove, 0.15f) then Actions.run {
                    eventBus.enqueueEventSync(UnitAttackAnimationComplete(id))
                }
                vm.addActorAction(ActorActionTarget.IMAGE, action)
            }
        }
    }

    @HandlesEvent
    suspend fun unitUsingAbility(event: UnitUsingAbilityEvent) {
        if (!filterEvent(event)) return

        onRenderingThread {
            withUnit {
                val action = ability.animation then Actions.run {
                    eventBus.enqueueEventSync(UnitAbilityAnimationComplete(id))
                }
                vm.addActorAction(ActorActionTarget.IMAGE, action)
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
    private fun withUnit(callback: suspend Entity.() -> Unit) = engine.withUnit(unitId) { entity -> onRenderingThread { entity.callback() } }
}
