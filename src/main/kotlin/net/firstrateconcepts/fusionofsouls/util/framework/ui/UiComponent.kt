package net.firstrateconcepts.fusionofsouls.util.framework.ui

import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import net.firstrateconcepts.fusionofsouls.config.inject
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.TableView

@Scene2dDsl
@Suppress("UNCHECKED_CAST")
inline fun <S, reified C : Controller, V : TableView> KWidget<S>.uiComponent(init: V.(S) -> Unit = {}): V {
    val component = inject<C>()
    component.load()
    component.view.init()
    return actor(component.view as V, init)
}
