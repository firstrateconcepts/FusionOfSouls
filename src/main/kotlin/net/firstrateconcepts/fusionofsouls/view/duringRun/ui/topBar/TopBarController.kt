package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import net.firstrateconcepts.fusionofsouls.model.event.enqueueShowDialog
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.uiComponent
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu.MenuDialogController

@Scene2dDsl
inline fun <S> KWidget<S>.topBar(init: TopBarView.(S) -> Unit = {}) = uiComponent<S, TopBarController, TopBarView>(init)

class TopBarController(private val eventBus: EventBus) : Controller {
    override val vm = TopBarViewModel()
    override val view = TopBarView(this, vm)

    fun heroButtonClicked() = Unit
    fun bossButtonClicked() = Unit
    fun menuButtonClicked() = eventBus.enqueueShowDialog<MenuDialogController>()
}
