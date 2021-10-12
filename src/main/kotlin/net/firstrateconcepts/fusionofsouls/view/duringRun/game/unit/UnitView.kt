package net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit

import com.badlogic.gdx.utils.Align
import ktx.scene2d.progressBar
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visTable
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindUpdatable
import net.firstrateconcepts.fusionofsouls.util.ext.ui.progressBarStyleHeight
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.GroupView

class UnitView(override val controller: UnitController, override val vm: UnitViewModel) : GroupView(controller, vm) {
    private val logger = fosLogger()

    override fun init() {
        val vm = vm

        setSize(0.75f, 0.75f)
        setBounds(0f, 0f, 0.75f, 0.75f)
        setOrigin(Align.center)
        bindUpdatable(vm.position) { vm.position.get().apply { setPosition(x, y) } }
        bindUpdatable(vm.actorActions) {
            this@UnitView.logger.debug { "Adding actor action for unit [${vm.id}]" }
            val actions = vm.actorActions.get()
            while (actions.isNotEmpty()) this@UnitView.addAction(actions.removeFirst())
        }

        progressBarStyleHeight("unitBar", 0.1f)

        visTable {
            setRound(false)
            setSize(0.75f, 0.3f)
            y = 1f

            progressBar(style = "unitBar") {
                bindUpdatable(vm.attackTimerPercent) {
                    this@UnitView.logger.info { "${vm.id} new attack percent ${vm.attackTimerPercent.get()}" }
                    value = vm.attackTimerPercent.get()
                }

                setAnimateDuration(0.25f)
                setSize(0.75f, 0.1f)
                setOrigin(Align.center)
                setRound(false)
            }.cell(height = 0.1f, width = 0.75f, row = true)
        }

        visImage(vm.texture) {
            setSize(0.75f, 0.75f)
            setOrigin(Align.center)
            bindUpdatable(vm.rotation) { vm.rotation.get().apply { rotation = this } }
        }
    }
}
