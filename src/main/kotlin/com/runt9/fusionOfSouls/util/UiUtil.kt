package com.runt9.fusionOfSouls.util

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kotcrab.vis.ui.VisUI
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultHorizontalStyle
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.style.progressBar
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.properties.Delegates

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
inline fun <S> KWidget<S>.fosVisTable(
    defaultSpacing: Boolean = true,
    init: KVisTable.(S) -> Unit = {}
): KVisTable {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(KVisTable(defaultSpacing)) { s ->
        setFillParent(true)
        defaults()
        this.init(s)
    }
}

fun rectPixmapTexture(width: Int, height: Int, color: Color): Texture {
    val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fillRectangle(0, 0, pixmap.width, pixmap.height)
    return Texture(pixmap)
}

fun Texture.toDrawable() = TextureRegionDrawable(this)
fun <S> KWidget<S>.rectPixmap(width: Int, height: Int, color: Color) = visImage(rectPixmapTexture(width, height, color))
fun <S> KWidget<S>.squarePixmap(size: Int, color: Color) = rectPixmap(size, size, color)

fun progressBarStyleHeight(styleName: String, height: Float) {
    VisUI.getSkin().progressBar(styleName, defaultHorizontalStyle) {
        background = TextureRegionDrawable((background as TextureRegionDrawable).region)
        background.minHeight = height
        knob = TextureRegionDrawable((knob as TextureRegionDrawable).region)
        knob.minHeight = height
        knobBefore = TextureRegionDrawable((knobBefore as TextureRegionDrawable).region)
        knobBefore.minHeight = height
    }
}

@Scene2dDsl
fun <S, T> KWidget<S>.observableLabel(listenerList: MutableList<(T) -> Unit>, stringSupplier: () -> String) = visLabel(stringSupplier()) {
    listenerList += {
        setText(stringSupplier())
    }
}

fun <T> simpleObservable(initialValue: T, listeners: List<(T) -> Unit>) = Delegates.observable(initialValue) { _, _, newValue -> listeners.forEach { it(newValue) } }
