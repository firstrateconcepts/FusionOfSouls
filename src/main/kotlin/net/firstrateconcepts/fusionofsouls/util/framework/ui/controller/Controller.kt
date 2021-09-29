package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.util.ext.FosStage
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.View

abstract class Controller : Disposable {
    abstract val vm: ViewModel
    abstract val view: View

    protected var stage: FosStage? = null

    open fun render(delta: Float) = Unit

    open fun addedToStage(stage: FosStage) {
        this.stage = stage
    }

    open fun removedFromStage() {
        stage = null
    }

    override fun dispose() {
        view.dispose()
        vm.dispose()
    }
}
