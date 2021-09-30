package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.View

interface Controller : Disposable {
    val vm: ViewModel
    val view: View

    override fun dispose() {
        view.dispose()
        vm.dispose()
    }
}
