package com.runt9.fusionOfSouls.view.duringRun.unitBench

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.SnapshotArray
import com.kotcrab.vis.ui.widget.Draggable
import com.kotcrab.vis.ui.widget.VisImage
import com.runt9.fusionOfSouls.benchBarHeight
import com.runt9.fusionOfSouls.maxInactiveUnits
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.hero.Hero
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.squarePixmap
import com.runt9.fusionOfSouls.view.BattleUnit
import com.runt9.fusionOfSouls.view.duringRun.UnitDraggingDragPane
import com.runt9.fusionOfSouls.view.duringRun.UnitGridTable
import ktx.scene2d.KStack
import ktx.scene2d.stack

class UnitBenchGridTable : UnitGridTable() {
    override val gridSquares: List<VisImage>

    init {
        val grid = mutableListOf<VisImage>()

        repeat(maxInactiveUnits) {
            stack {
                grid += squarePixmap(benchBarHeight, Color.LIGHT_GRAY)
            }.cell(space = 2f)
        }

        gridSquares = grid.toList()
    }
}
class UnitBenchDragPane(private val battleManager: BattleManager) : UnitDraggingDragPane(UnitBenchGridTable()) {
    override fun accept(actor: Actor): Boolean {
        if (actor is GameUnit) {
            return true
        }

        if (actor is BattleUnit) {
            if (actor.unit is Hero) {
                return false
            }

            return runState.inactiveUnits.size < maxInactiveUnits
        }

        return super.accept(actor)
    }

    override fun getChildren(): SnapshotArray<Actor> =
        SnapshotArray(actor.children.map { it as KStack }.flatMap { it.children }.filterIsInstance<GameUnit>().toTypedArray())

    override fun addToProperStack(actor: Actor) {
        getStacks().find { s -> s.children.none { c -> c is GameUnit } }?.add(actor)
    }

    override fun getActorToAdd(actor: Actor) = if (actor is BattleUnit) putUnitOnBench(actor) else actor

    private fun putUnitOnBench(unit: BattleUnit): BasicUnit {
        unit.listeners.removeAll { it is Draggable }

        val basicUnit = unit.unit as BasicUnit

        runState.deactivateUnit(basicUnit)

        return basicUnit
    }
}
