package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit.UnitViewModel

class DuringRunGameViewModel : ViewModel() {
    val units = Binding(listOf<UnitViewModel>())
}
