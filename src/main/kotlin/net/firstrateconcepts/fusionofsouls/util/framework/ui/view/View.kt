package net.firstrateconcepts.fusionofsouls.util.framework.ui.view

import com.badlogic.gdx.utils.Disposable
import ktx.scene2d.KTable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Updatable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.Controller

interface View : Disposable, Updatable, KTable {
    val controller: Controller
    val vm: ViewModel
    fun init()
    override fun update() = Unit
}
