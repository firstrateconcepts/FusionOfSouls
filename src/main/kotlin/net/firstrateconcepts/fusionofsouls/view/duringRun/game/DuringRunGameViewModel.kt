package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class DuringRunGameViewModel : ViewModel() {
    val units = Binding(listOf<UnitViewModel>())
}
