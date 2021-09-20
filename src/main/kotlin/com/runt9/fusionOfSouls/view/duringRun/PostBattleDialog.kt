package com.runt9.fusionOfSouls.view.duringRun

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisDialog
import com.kotcrab.vis.ui.widget.VisTextButton
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.loot.Rune
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.service.UnitGenerator
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.squarePixmap
import com.runt9.fusionOfSouls.viewportHeight
import com.soywiz.kmem.toIntFloor
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.scene2d.KTable
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton

class PostBattleDialog(val unitGenerator: UnitGenerator, private val listener: () -> Unit) : VisDialog("Rewards"), KTable {
    init {
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        isModal = false
        button("Next Battle")
        key(Input.Keys.ESCAPE, null)

        val buttonHandler: VisTextButton.(KVisTable) -> Unit = { table: KVisTable ->
            this += Actions.fadeOut(0.1f) then Actions.removeActor()
            val cell = table.getCell(this)
            cell.height(0f).space(0f)
        }

        val gold = generateGold()
        val xp = calculateXp()
        contentTable.apply {
            val group = scene2d.visTable(defaultSpacing = true) {
                val table = this
                visTextButton("$gold Gold") {
                    onClick {
                        runState.gold += gold
                        buttonHandler(table)
                    }
                }.cell(growX = true, row = true)

                visTextButton("$xp Hero XP") {
                    onClick {
                        if (runState.hero.addXp(xp)) {
                            table.visTextButton("Hero level up!") {
                                onClick {
                                    runState.hero.primaryAttrs.all.forEach {
                                        it.addModifier(AttributeModifier(percentModifier = 10.0))
                                    }
                                    buttonHandler(table)
                                }
                            }.cell(growX = true, row = true)
                        }
                        buttonHandler(table)
                    }
                }.cell(growX = true, row = true)

                visTextButton("Unit or Rune") {
                    onClick {
                        val dialog = this@PostBattleDialog.UnitRuneSelectionDialog {
                            buttonHandler(table)
                        }

                        dialog.show(stage)
                    }
                }.cell(growX = true, row = true)
            }
            add(group).growX().expandY().pad(10f).align(Align.top)
//            debugAll()
        }
    }

    override fun getPrefWidth() = battleWidth * 0.25f
    override fun getPrefHeight() = viewportHeight * 0.75f

    override fun result(result: Any?) {
        listener()
    }

    // TODO: This is not where these functions go
    private fun generateGold(): Int {
        var gold = runState.floor
        gold += runState.battleContext.enemyCount
        if (runState.battleContext.flawless) {
            gold++
        }
        gold += ((runState.gold + gold) / 10.0).toIntFloor()
        return gold
    }

    private fun calculateXp(): Int {
        if (!runState.battleContext.heroLived) {
            return 0
        }

        var xp = runState.floor
        xp += runState.battleContext.enemyCount
        return xp
    }

    inner class UnitRuneSelectionDialog(private val listener: () -> Unit) : VisDialog("Choose Reward"), KTable {
        private val unit = this@PostBattleDialog.unitGenerator.generateUnit(Rarity.COMMON, Texture(Gdx.files.internal("blueArrow-tp.png")))
        private val rune = Rune(Rarity.COMMON)

        init {
            setOrigin(Align.center)
            centerWindow()
            isMovable = false
            isModal = false

            contentTable.apply {
                val group = scene2d.visTable(defaultSpacing = true) {
                    stack {
                        container { squarePixmap(40, Color.LIGHT_GRAY) }
                        container { actor(this@UnitRuneSelectionDialog.unit) }

                        onClick {
                            runState.addNewUnit(this@UnitRuneSelectionDialog.unit)
                            this@UnitRuneSelectionDialog.listener()
                            this@UnitRuneSelectionDialog.hide()
                        }
                    }

                    stack {
                        container { squarePixmap(40, Color.LIGHT_GRAY) }
                        container { this += this@UnitRuneSelectionDialog.rune }

                        onClick {
                            runState.unequippedRunes.add(this@UnitRuneSelectionDialog.rune)
                            this@UnitRuneSelectionDialog.listener()
                            this@UnitRuneSelectionDialog.hide()
                        }
                    }
                }

                add(group).grow().pad(10f)
            }
        }

        override fun getPrefWidth() = battleWidth * 0.25f
        override fun getPrefHeight() = viewportHeight * 0.5f
    }
}
