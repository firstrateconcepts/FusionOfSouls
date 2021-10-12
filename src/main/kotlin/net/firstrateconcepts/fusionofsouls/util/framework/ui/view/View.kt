package net.firstrateconcepts.fusionofsouls.util.framework.ui.view

import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Updatable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

interface View : Disposable, Updatable {
    val controller: Controller
    val vm: ViewModel
    fun init()
    override fun update() = Unit
}
