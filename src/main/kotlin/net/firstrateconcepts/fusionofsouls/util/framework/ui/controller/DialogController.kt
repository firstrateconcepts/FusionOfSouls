package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.FosStage
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.DialogView

abstract class DialogController : Controller {
    abstract override val view: DialogView
    private var stage: FosStage? = null

    var isShown = false

    fun show(stage: FosStage) {
        if (!isShown) {
            this.stage = stage
            view.show(stage)
            view.init()
            isShown = true
        }
    }

    fun hide() {
        if (isShown) {
            view.hide()
            isShown = false
            stage = null
        }
    }
}
