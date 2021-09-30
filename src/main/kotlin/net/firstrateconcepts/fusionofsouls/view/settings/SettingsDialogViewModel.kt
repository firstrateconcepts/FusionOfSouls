package net.firstrateconcepts.fusionofsouls.view.settings

import net.firstrateconcepts.fusionofsouls.model.config.PlayerSettings
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class SettingsDialogViewModel(settings: PlayerSettings) : ViewModel() {
    val fullscreen = Binding(settings.fullscreen)
    val vsync = Binding(settings.vsync)
}
