package com.runt9.fusionOfSouls.screen.duringRun.charDialog.tab

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.layout.DragPane
import com.kotcrab.vis.ui.layout.FlowGroup
import com.kotcrab.vis.ui.widget.Draggable
import com.runt9.fusionOfSouls.model.loot.rune.Rune
import com.runt9.fusionOfSouls.service.runState
import ktx.scene2d.KGroup
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.scene2d
import ktx.scene2d.splitPane
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visScrollPane
import ktx.scene2d.vis.visTable

class RuneTab : CharDialogTab("Runes") {
    private val equippedPane = EquippedRunesDragPane()
    private val inventoryPane = InventoryDragPane()

    init {
        (contentTable as KVisTable).apply {
            splitPane(vertical = true) {
                maxSplitAmount = 0.5f
                minSplitAmount = 0.5f

                setFirstWidget(this@RuneTab.drawRunePanePortion(this@RuneTab.equippedPane) { "Equipped: (${runState.hero.runes.size} / ${runState.runeCap})" })
                setSecondWidget(this@RuneTab.drawRunePanePortion(this@RuneTab.inventoryPane) { "Inventory (${runState.unequippedRunes.size})" })
            }.cell(grow = true)
        }
    }

    @Scene2dDsl
    private fun drawRunePanePortion(dragPane: DragPane, header: () -> String) = scene2d.visTable {
        visLabel(header.invoke(), "small") {
            val listener = { event: Event ->
                if (event is RuneCountUpdateEvent) {
                    setText(header.invoke())
                }

                false
            }

            this@RuneTab.equippedPane.addListener(listener)
            this@RuneTab.inventoryPane.addListener(listener)
        }.cell(growX = true, row = true, padLeft = 5f)
        visScrollPane {
            setScrollingDisabled(true, false)
            setFlickScroll(false)
            addActor(dragPane)
        }.cell(grow = true)
    }
}

class RuneCountUpdateEvent(target: Actor) : Event() {
    init {
        bubbles = true
        setTarget(target)
    }
}

class EquippedRunesDragPane : DragPane(FlowGroup(false, 6f)), KGroup {
    private var initialized = false

    init {
        align(Align.topLeft)
        draggable = Draggable()
        draggable.isInvisibleWhenDragged = true

        runState.hero.runes.forEach(this::addActor)
        initialized = true
    }

    override fun accept(actor: Actor) = if (runState.hero.runes.size >= runState.runeCap) false else super.accept(actor)
    override fun doOnAdd(actor: Actor) {
        super.doOnAdd(actor)
        if (initialized) {
            val rune = actor as Rune
            runState.apply {
                unequippedRunes.remove(rune)
                hero.addRune(rune)
            }
            notify(RuneCountUpdateEvent(this), false)
        }
    }
}

class InventoryDragPane : DragPane(FlowGroup(false, 6f)), KGroup {
    private var initialized = false

    init {
        align(Align.topLeft)
        draggable = Draggable()
        draggable.isInvisibleWhenDragged = true

        runState.unequippedRunes.forEach(this::addActor)
        initialized = true
    }

    override fun doOnAdd(actor: Actor) {
        super.doOnAdd(actor)
        if (initialized) {
            val rune = actor as Rune
            runState.apply {
                unequippedRunes.add(rune)
                hero.removeRune(rune)
            }
            notify(RuneCountUpdateEvent(this), false)
        }
    }
}
