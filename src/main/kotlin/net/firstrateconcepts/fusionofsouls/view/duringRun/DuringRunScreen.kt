package net.firstrateconcepts.fusionofsouls.view.duringRun

import net.firstrateconcepts.fusionofsouls.util.framework.ui.core.GameScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController

class DuringRunScreen(override val gameController: DuringRunGameController, override val uiController: DuringRunUiController) : GameScreen(16f, 9f)
