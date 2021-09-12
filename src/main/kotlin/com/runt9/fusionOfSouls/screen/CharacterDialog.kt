package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisDialog
import com.kotcrab.vis.ui.widget.VisImage
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.runt9.fusionOfSouls.battleWidth
import com.runt9.fusionOfSouls.resourceBarHeight
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.viewportHeight
import com.soywiz.kmem.toIntRound
import com.soywiz.korma.math.roundDecimalPlaces
import ktx.scene2d.KTable
import ktx.scene2d.vis.addTabContentsTo
import ktx.scene2d.vis.tab
import ktx.scene2d.vis.tabbedPane
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable

class CharacterDialog(title: String) : VisDialog(title), KTable {
    val leftPane: VisTable
    val rightPane: VisTable
    val charInfo: VisTable
    val tabLayout: VisTable
    val tabBar: VisTable
    val tabContent: VisTable
    val tabbedPane: TabbedPane
    val runesTab: Tab
    val attrsTab: Tab
    val abilityTab: Tab
//    val fusionView: VisScrollPane

    init {
        setOrigin(Align.center)
        centerWindow()
        isMovable = false
        button("Done")
        key(Input.Keys.ESCAPE, null)
//        debug = true

        contentTable.apply {
            leftPane = VisTable().apply {
                debugAll()
                charInfo = buildCharInfo()
                add(charInfo).growX().height(80f).row()

                tabbedPane = tabbedPane {
                    this@CharacterDialog.runesTab = tab("Runes", closeableByUser = false)
                    this@CharacterDialog.attrsTab = tab("Attributes", closeableByUser = false) {
                        visTable {
                            val attrs = runState.hero.secondaryAttrs
                            defaults().left().space(0f)
                            visLabel("Max HP")
                            defaults().center().space(0f)
                            visLabel(attrs.maxHp.value.toIntRound().toString())
                            row()
                            defaults().left().space(0f)
                            visLabel("Base Damage")
                            defaults().center().space(0f)
                            visLabel(attrs.baseDamage.value.toIntRound().toString())
                            row()
                            defaults().left().space(0f)
                            visLabel("Skill Multiplier")
                            defaults().center().space(0f)
                            visLabel("${attrs.skillMulti.value.roundDecimalPlaces(2)}x")
                            row()
                            defaults().left().space(0f)
                            visLabel("Defense")
                            defaults().center().space(0f)
                            visLabel("${attrs.defense.value.toIntRound()}%")
                            row()
                            defaults().left().space(0f)
                            visLabel("Evasion")
                            defaults().center().space(0f)
                            visLabel(attrs.evasion.value.toIntRound().toString())
                            row()
                            defaults().left().space(0f)
                            visLabel("Crit Threshold")
                            defaults().center().space(0f)
                            visLabel(attrs.critThreshold.value.toIntRound().toString())
                            row()
                            defaults().left().space(0f)
                            visLabel("Crit Multiplier")
                            defaults().center().space(0f)
                            visLabel("${attrs.critBonus.value.roundDecimalPlaces(2)}x")
                            row()
                            defaults().left().space(0f)
                            visLabel("Attacks / second")
                            defaults().center().space(0f)
                            visLabel(attrs.attackSpeed.value.roundDecimalPlaces(2).toString())
                            row()
                            defaults().left().space(0f)
                            visLabel("Cooldown Reduction")
                            defaults().center().space(0f)
                            visLabel("${attrs.cooldownReduction.value.roundDecimalPlaces(2)}x")
                        }
                    }
                    this@CharacterDialog.abilityTab = tab("Ability", closeableByUser = false)
                    switchTab(0)
                }
                tabLayout = VisTable().apply {
                    tabBar = tabbedPane.table
                    add(tabBar).growX().row()
                    // TOOD: get from pane listener
                    tabContent = VisTable()
                    add(tabContent).grow()
                }
                add(tabLayout).grow()
                tabbedPane.addTabContentsTo(tabContent)
            }

            add(leftPane).grow()

            rightPane = VisTable().apply {

            }

            add(rightPane).grow()
        }
    }

    override fun getPrefWidth(): Float {
        return (battleWidth * 0.75).toFloat()
    }

    override fun getPrefHeight(): Float {
        return viewportHeight.toFloat() - resourceBarHeight
    }
    
    private fun buildCharInfo() = VisTable().apply {
        val leftInfo = VisTable().apply {
            val charPic = VisImage().apply {
                setSize(40f, 40f)
            }
            defaults().grow()
            add(charPic)
            row()
            
            val level = VisLabel("Level: 1")
            val class1 = VisLabel("Ranger")
            val class2 = VisLabel("Fighter")

            add(level).grow().row()
            add(class1).grow().row()
            add(class2).grow()
        }
        defaults().grow().space(0f)
        add(leftInfo)
        
        val rightInfo = VisTable().apply { 
            val attrs = runState.hero.primaryAttrs
            val body = VisLabel("Body: ${attrs.body.value.toIntRound()}")
            add(body).grow().row()
            val mind = VisLabel("Mind: ${attrs.mind.value.toIntRound()}")
            add(mind).grow().row()
            val instinct = VisLabel("Instinct: ${attrs.instinct.value.toIntRound()}")
            add(instinct).grow().row()
            val luck = VisLabel("Luck: ${attrs.luck.value.toIntRound()}")
            add(luck).grow()
        }
        
        add(rightInfo).grow()
    }
}
