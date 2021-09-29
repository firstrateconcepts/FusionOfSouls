package net.firstrateconcepts.fusionofsouls.util.framework.ui

import com.badlogic.gdx.utils.Disposable

interface Controller : Disposable {
    val vm: ViewModel
    val view: View

    fun render(delta: Float) = Unit
    fun addedToStage() = Unit
    fun removedFromStage() = Unit

    override fun dispose() {
        view.dispose()
        vm.dispose()
    }
}
