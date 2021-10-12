package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitBench

import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent

@Scene2dDsl
fun <S> KWidget<S>.unitBench(init: UnitBenchView.(S) -> Unit = {}) = uiComponent<S, UnitBenchController, UnitBenchView>(init = init)

class UnitBenchController : Controller {
    override val vm = UnitBenchViewModel()
    override val view = UnitBenchView(this, vm)
}
