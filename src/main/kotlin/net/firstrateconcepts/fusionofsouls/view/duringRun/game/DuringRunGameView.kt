package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import ktx.actors.centerPosition
import ktx.scene2d.vis.KFloatingGroup
import ktx.scene2d.vis.floatingGroup
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.TableView
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_HEIGHT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_WIDTH
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit.unit

class DuringRunGameView(override val controller: DuringRunGameController, override val vm: DuringRunGameViewModel) : TableView(controller, vm) {
    private val logger = fosLogger()
    private var unitPanel: KFloatingGroup? = null

    override fun init() {
        val vm = vm
        val controller = controller

        setSize(GAME_WIDTH, GAME_HEIGHT)
        centerPosition()

        // TODO: Make this work with add/remove from binding list instead of clearing and redrawing every time
        unitPanel = floatingGroup {
            vm.units.bind {
                this@DuringRunGameView.logger.info { "Updating all units" }
                clear()
                controller.clearChildren()
                vm.units.get().forEach { unit -> unit(unit) { controller.addChild(this.controller) } }
            }
        }.cell(row = true, grow = true)
    }

    override fun dispose() {
        unitPanel = null
        super.dispose()
    }
}
