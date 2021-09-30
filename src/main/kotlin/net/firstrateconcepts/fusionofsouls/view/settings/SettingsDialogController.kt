package net.firstrateconcepts.fusionofsouls.view.settings

import com.badlogic.gdx.Graphics
import net.firstrateconcepts.fusionofsouls.config.PlayerSettingsConfig
import net.firstrateconcepts.fusionofsouls.model.config.PlayerSettings
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController

class SettingsDialogController(private val settingsConfig: PlayerSettingsConfig, graphics: Graphics) : DialogController() {
    private var currentSettings = settingsConfig.get()
    override val vm = SettingsDialogViewModel(currentSettings)
    override val view = SettingsDialogView(this, vm, graphics.width, graphics.height)

    fun applySettings() {
        val newSettings = PlayerSettings(
            fullscreen = vm.fullscreen.get(),
            vsync = vm.vsync.get(),
            // NB: Not going to be in the dialog
            logLevel = currentSettings.logLevel,
            // TODO: Add resolution selection to dialog
            resolution = currentSettings.resolution
        )

        settingsConfig.apply(newSettings)
        // TODO: Confirm settings work before saving
        settingsConfig.save(newSettings)
        currentSettings = newSettings
        vm.saveCurrent()
    }
}
