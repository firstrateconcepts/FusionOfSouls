package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitBench

import com.badlogic.gdx.graphics.Color
import ktx.scene2d.stack
import net.firstrateconcepts.fusionofsouls.util.ext.ui.squarePixmap
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.TableView

class UnitBenchView(override val controller: UnitBenchController, override val vm: UnitBenchViewModel) : TableView(controller, vm) {
    override fun init() {
        repeat(20) {
            stack {
                squarePixmap(60, Color.LIGHT_GRAY)
            }.cell(space = 2f)
        }
    }
}
