package com.runt9.fusionOfSouls.view.duringRun.charDialog.tab

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import ktx.scene2d.KTable
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTable

abstract class CharDialogTab(private val title: String, ) : Tab(false, false), KTable {
    private val content = scene2d.visTable()
    override fun <T : Actor> add(actor: T): Cell<T> = content.add(actor)
    override fun getContentTable(): Table = content
    override fun getTabTitle(): String = title
}
