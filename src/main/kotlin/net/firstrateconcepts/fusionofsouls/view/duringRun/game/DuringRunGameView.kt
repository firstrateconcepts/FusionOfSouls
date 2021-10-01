package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import ktx.actors.centerPosition
import ktx.scene2d.vis.KFloatingGroup
import ktx.scene2d.vis.floatingGroup
import ktx.scene2d.vis.visImage
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.TableView

class DuringRunGameView(override val controller: DuringRunGameController, override val vm: DuringRunGameViewModel) : TableView(controller, vm) {
    private var unitPanel: KFloatingGroup? = null

    override fun init() {
        setSize(16f, 7f)
        centerPosition()

        unitPanel = floatingGroup {
            this@DuringRunGameView.vm.units.forEach(this::drawUnit)
        }.cell(row = true, grow = true)
    }

    fun addUnit(unit: UnitViewModel) = unitPanel?.drawUnit(unit)

    override fun dispose() {
        unitPanel = null
        super.dispose()
    }
}

fun KFloatingGroup.drawUnit(unit: UnitViewModel) {
    visImage(unit.texture) {
        setSize(1f, 1f)
        unit.position.bind {
            val position = unit.position.get()
            this@visImage.setPosition(position.x, position.y)
        }
    }
}
