package net.firstrateconcepts.fusionofsouls.util.framework.ui

import com.badlogic.gdx.utils.Disposable
import com.kotcrab.vis.ui.widget.VisTable
import ktx.scene2d.KTable

abstract class View : VisTable(), KTable, Disposable, Updatable {
    abstract val controller: Controller
    abstract val vm: ViewModel

    abstract fun init()

    override fun update() = Unit
}

abstract class ScreenView : View() {
    override fun init() {
        setSize(stage.width, stage.height)
    }
}
