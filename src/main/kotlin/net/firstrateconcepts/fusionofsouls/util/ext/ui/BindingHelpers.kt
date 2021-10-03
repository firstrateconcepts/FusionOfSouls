package net.firstrateconcepts.fusionofsouls.util.ext.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import ktx.actors.onChange
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Updatable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.updatable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

fun <T : Any> Button.bindButtonDisabled(
    binding: ViewModel.Binding<T>,
    disabledValue: T,
    evaluateOnCall: Boolean = true
) = bindUpdatable(binding, evaluateOnCall) {
    isDisabled = binding.get() == disabledValue
    touchable = if (isDisabled) Touchable.disabled else Touchable.enabled
}

fun Button.bindButtonDisabledToVmDirty(vm: ViewModel, disabledValue: Boolean, evaluateOnCall: Boolean = true) {
    bindButtonDisabled(vm.dirty, disabledValue, evaluateOnCall)
}

fun CheckBox.bindChecked(binding: ViewModel.Binding<Boolean>) {
    isChecked = binding.get()
    onChange { binding.set(isChecked) }
}

fun Label.bindLabelText(strGetter: Updatable.() -> String) = updatable { setText(strGetter()) }.update()

fun <T : Any> Actor.bindVisible(
    binding: ViewModel.Binding<T>,
    visibleValue: T,
    evaluateOnCall: Boolean = true
) = bindUpdatable(binding, evaluateOnCall) {
    isVisible = binding.get() == visibleValue
    touchable = if (isVisible) Touchable.enabled else Touchable.disabled
}

fun <T : Any, A : Actor> A.bindUpdatable(binding: ViewModel.Binding<T>, evaluateOnCall: Boolean = true, updater: A.() -> Unit) {
    val updatable = updatable { updater() }

    binding.bind(updatable)

    if (evaluateOnCall) {
        updatable.update()
    }
}
