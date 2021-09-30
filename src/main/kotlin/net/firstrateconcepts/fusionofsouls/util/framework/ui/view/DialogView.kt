package net.firstrateconcepts.fusionofsouls.util.framework.ui.view

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisDialog
import ktx.scene2d.KTable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

abstract class DialogView(
    override val controller: DialogController,
    override val vm: ViewModel,
    name: String,
    private val screenWidth: Int,
    private val screenHeight: Int
) : VisDialog(name, "dialog"), View {
    protected abstract val widthScale: Float
    protected abstract val heightScale: Float

    override fun init() {
        setOrigin(Align.center)
        centerWindow()
        isMovable = false

        contentTable.wrapDsl().initContentTable()
        buttonsTable.wrapDsl().initButtons()
    }

    private fun Table.wrapDsl() = object : KTable { override fun <T : Actor> add(actor: T) = this@wrapDsl.add(actor) }

    protected abstract fun KTable.initContentTable()
    protected abstract fun KTable.initButtons()

    override fun hide() {
        super.hide()
        dispose()
        controller.isShown = false
    }

    override fun dispose() {
        contentTable.clear()
        buttonsTable.clear()
        remove()
    }

    override fun getPrefWidth() = screenWidth * widthScale
    override fun getPrefHeight() = screenHeight * heightScale
}
