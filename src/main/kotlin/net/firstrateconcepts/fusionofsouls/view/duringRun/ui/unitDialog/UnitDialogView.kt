package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog

import ktx.scene2d.KTable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.DialogView

class UnitDialogView(
    override val controller: UnitDialogController,
    override val vm: UnitDialogViewModel,
    screenWidth: Int,
    screenHeight: Int
) : DialogView(controller, vm, "Unit", screenWidth, screenHeight) {
    override val widthScale = 0.75f
    override val heightScale = 0.9f

    override fun KTable.initContentTable() {

    }

    override fun KTable.initButtons() { closeButton() }
}
