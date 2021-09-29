package net.firstrateconcepts.fusionofsouls.view.loading

import com.badlogic.gdx.utils.Align
import ktx.scene2d.vis.visLabel
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.percent
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindLabelText
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.ScreenView
import kotlin.math.roundToInt

class LoadingScreenView(override val controller: LoadingScreenController, override val vm: LoadingScreenViewModel) : ScreenView(controller, vm) {
    private val logger = fosLogger()

    override fun init() {
        logger.debug { "Initializing" }
        super.init()
        visLabel("", "title-plain") {
            bindLabelText { "Loading ${this@LoadingScreenView.vm.loadingPercent().percent().roundToInt()}%" }
        }.cell(expand = true, align = Align.center)
    }
}
