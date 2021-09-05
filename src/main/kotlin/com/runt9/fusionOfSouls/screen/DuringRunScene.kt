package com.runt9.fusionOfSouls.screen

import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.RunState
import ktx.app.KtxScreen


class DuringRunScene(private val runState: RunState, private val battleManager: BattleManager) : KtxScreen {
    private val benchBarHeight = cellSize
    private var previousSpeed = 1.0
    private val debugMode = false

    override fun show() {
    }
}
