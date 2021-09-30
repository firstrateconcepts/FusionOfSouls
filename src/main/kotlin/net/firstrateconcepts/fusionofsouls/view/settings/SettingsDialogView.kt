package net.firstrateconcepts.fusionofsouls.view.settings

import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.scene2d.KTable
import ktx.scene2d.checkBox
import ktx.scene2d.textButton
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindButtonDisabledToVmDirty
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindChecked
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.DialogView

class SettingsDialogView(
    override val controller: SettingsDialogController,
    override val vm: SettingsDialogViewModel,
    screenWidth: Int,
    screenHeight: Int
) : DialogView(controller, vm, "Settings", screenWidth, screenHeight) {
    override val widthScale = 0.33f
    override val heightScale = 0.5f

    override fun KTable.initContentTable() {
        val vm = this@SettingsDialogView.vm

        checkBox("Fullscreen", "switch") { bindChecked(vm.fullscreen) }.cell(row = true, align = Align.left)
        checkBox("VSync", "switch") { bindChecked(vm.vsync) }.cell(row = true, align = Align.left)
    }

    override fun KTable.initButtons() {
        val vm = this@SettingsDialogView.vm
        val controller = this@SettingsDialogView.controller

        textButton("Apply") {
            bindButtonDisabledToVmDirty(vm, false)
            onChange { controller.applySettings() }
        }

        // TODO: Confirmation dialog if unsaved changes
        textButton("Done") {
            onChange { controller.hide() }
        }
    }
}

