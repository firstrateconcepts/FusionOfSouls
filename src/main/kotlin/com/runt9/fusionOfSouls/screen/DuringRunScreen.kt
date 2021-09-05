package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.RunState


class DuringRunScreen(private val game: FosGame, private val runState: RunState, private val battleManager: BattleManager, override val stage: Stage) : FosScreen {
    private val benchBarHeight = cellSize
    private var previousSpeed = 1.0
    private val debugMode = false

    override fun show() {
    }
}
