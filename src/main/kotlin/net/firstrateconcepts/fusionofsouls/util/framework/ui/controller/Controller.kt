package net.firstrateconcepts.fusionofsouls.util.framework.ui.controller

import com.badlogic.gdx.utils.Disposable
import net.firstrateconcepts.fusionofsouls.model.event.changeScreenRequest
import net.firstrateconcepts.fusionofsouls.util.ext.FosScreen
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.View

abstract class Controller : Disposable {
    abstract val vm: ViewModel
    abstract val view: View

    override fun dispose() {
        view.dispose()
        vm.dispose()
    }

    inline fun <reified S : FosScreen> EventBus.enqueueChangeScreen() = enqueueEventSync(changeScreenRequest<S>())
}
