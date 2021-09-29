package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.DialogView

abstract class DialogController : Controller() {
    abstract override val view: DialogView

    var isShown = false

    fun show() {
        if (!isShown) {
            view.show(stage)
            isShown = true
        }
    }

    fun hide() {
        if (isShown) {
            view.hide()
            stage?.detachController(this)
            isShown = false
        }
    }
}
