package net.firstrateconcepts.fusionofsouls.util.ext.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.scene2d.KWidget
import ktx.scene2d.vis.visImage

fun rectPixmapTexture(width: Int, height: Int, color: Color): Texture {
    val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fillRectangle(0, 0, pixmap.width, pixmap.height)
    return Texture(pixmap)
}

fun Texture.toDrawable() = TextureRegionDrawable(this)
fun <S> KWidget<S>.rectPixmap(width: Int, height: Int, color: Color) = visImage(rectPixmapTexture(width, height, color))
fun <S> KWidget<S>.squarePixmap(size: Int, color: Color) = rectPixmap(size, size, color)
