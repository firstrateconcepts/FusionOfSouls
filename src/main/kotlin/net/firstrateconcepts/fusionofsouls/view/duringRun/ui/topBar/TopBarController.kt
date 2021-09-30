package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent

@Scene2dDsl
inline fun <S> KWidget<S>.topBar(init: TopBarView.(S) -> Unit = {}) = uiComponent<S, TopBarController, TopBarView>(init)

class TopBarController : Controller {
    override val vm = TopBarViewModel()
    override val view = TopBarView(this, vm)

    fun heroButtonClicked() = Unit
    fun bossButtonClicked() = Unit
    fun menuButtonClicked() = Unit
}
