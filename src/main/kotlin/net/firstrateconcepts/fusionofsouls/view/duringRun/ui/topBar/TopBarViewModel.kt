package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel

class TopBarViewModel : ViewModel() {
    val gold = Binding(0)
    val activeUnits = Binding(0)
    val unitCap = Binding(0)
    val floor = Binding(1)
    val room = Binding(1)
    val isDuringBattle = Binding(false)
}
