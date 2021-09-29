package net.firstrateconcepts.fusionofsouls.util.framework.ui.view

import com.kotcrab.vis.ui.widget.VisTable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel

abstract class TableView(override val controller: Controller, override val vm: ViewModel) : VisTable(), View {
    override fun update() = Unit

    override fun dispose() {
        clear()
        remove()
    }
}
