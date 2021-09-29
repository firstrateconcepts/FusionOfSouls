package net.firstrateconcepts.fusionofsouls.util.ext.ui

import com.kotcrab.vis.ui.widget.VisLabel
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Updatable

class AutoLabel(val strGetter: AutoLabel.() -> String, style: String) : VisLabel("", style), Updatable {
    private val logger = fosLogger()

    init {
        setText(strGetter())
    }

    override fun update() {
        logger.debug { "Updating value to ${strGetter()}" }
        setText(strGetter())
    }
}

@Scene2dDsl
inline fun <S> KWidget<S>.autoLabel(
    noinline strGetter: AutoLabel.() -> String,
    style: String = defaultStyle,
    init: (@Scene2dDsl AutoLabel).(S) -> Unit = {}
): AutoLabel = actor(AutoLabel(strGetter, style), init)
