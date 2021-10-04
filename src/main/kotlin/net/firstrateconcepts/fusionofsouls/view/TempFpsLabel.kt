package net.firstrateconcepts.fusionofsouls.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

abstract class IntValueLabel(text: CharSequence, var oldValue: Int, style: LabelStyle?) :
    Label(text.toString() + oldValue, style) {
    var appendIndex: Int

    abstract val value: Int

    override fun act(delta: Float) {
        val newValue = value
        if (oldValue != newValue) {
            oldValue = newValue
            val sb = text
            sb.setLength(appendIndex)
            sb.append(oldValue)
            invalidateHierarchy()
        }
        super.act(delta)
    }

    init {
        appendIndex = text.length
    }
}

class FpsLabel(text: CharSequence?, style: LabelStyle?) : IntValueLabel(text!!, -1, style) {
    constructor(text: CharSequence?, skin: Skin) : this(text, skin[LabelStyle::class.java])

    override val value: Int
        get() = Gdx.graphics.framesPerSecond
}

