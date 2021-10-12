package net.firstrateconcepts.fusionofsouls.util.framework.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.View

@Scene2dDsl
@Suppress("UNCHECKED_CAST")
inline fun <S, reified C : Controller, V : View> KWidget<S>.uiComponent(
    controllerInit: C.() -> Unit = {},
    noinline init: V.(S) -> Unit = {}
): V {
    val component = Injector.newInstanceOf<C>()
    component.controllerInit()
    component.load()
    component.view.init()
    return actor(component.view as Actor, init as Actor.(S) -> Unit) as V
}
