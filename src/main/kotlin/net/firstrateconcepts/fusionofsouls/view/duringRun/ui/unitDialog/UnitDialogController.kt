package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog

import com.badlogic.gdx.Graphics
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController

class UnitDialogController(graphics: Graphics) : DialogController() {
    override val vm = UnitDialogViewModel()
    override val view = UnitDialogView(this, vm, graphics.width, graphics.height)
}
