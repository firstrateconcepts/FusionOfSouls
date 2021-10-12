package net.firstrateconcepts.fusionofsouls.util.ext.ui

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.VisUI
import ktx.scene2d.defaultHorizontalStyle
import ktx.style.progressBar

fun progressBarStyleHeight(styleName: String, height: Float) {
    VisUI.getSkin().progressBar(styleName, defaultHorizontalStyle) {
        background = rectPixmapTexture(1, 1, Color.DARK_GRAY).toDrawable()
        background.minHeight = height
        background.minWidth = 0f
        background.leftWidth = 0f
        background.rightWidth = 0f
        background.topHeight = 0f
        background.bottomHeight = 0f
        knob = rectPixmapTexture(1, 1, Color.GREEN).toDrawable()
        knob.minHeight = height
        knob.minWidth = 0f
        knob.leftWidth = 0f
        knob.rightWidth = 0f
        knob.topHeight = 0f
        knob.bottomHeight = 0f
        knobBefore = rectPixmapTexture(1, 1, Color.ORANGE).toDrawable()
        knobBefore.minHeight = height
        knobBefore.minWidth = 0f
        knobBefore.leftWidth = 0f
        knobBefore.rightWidth = 0f
        knobBefore.topHeight = 0f
        knobBefore.bottomHeight = 0f
    }
}
