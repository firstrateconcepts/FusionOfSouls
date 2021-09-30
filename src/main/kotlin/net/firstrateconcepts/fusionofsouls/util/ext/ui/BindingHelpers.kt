package net.firstrateconcepts.fusionofsouls.util.ext.ui

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import ktx.actors.onChange
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Updatable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.updatable

fun <T : Any> Button.bindButtonDisabled(binding: ViewModel.Binding<T>, disabledValue: T, evaluateOnCall: Boolean = true) {
    val updatable = updatable {
        isDisabled = binding.get() == disabledValue
        touchable = if (isDisabled) Touchable.disabled else Touchable.enabled
    }

    binding.bind(updatable)

    if (evaluateOnCall) {
        updatable.update()
    }
}

fun Button.bindButtonDisabledToVmDirty(vm: ViewModel, disabledValue: Boolean, evaluateOnCall: Boolean = true) {
    bindButtonDisabled(vm.dirty, disabledValue, evaluateOnCall)
}

fun CheckBox.bindChecked(binding: ViewModel.Binding<Boolean>) {
    isChecked = binding.get()
    onChange { binding.set(isChecked) }
}

fun Label.bindLabelText(strGetter: Updatable.() -> String) = updatable { setText(strGetter()) }.update()
