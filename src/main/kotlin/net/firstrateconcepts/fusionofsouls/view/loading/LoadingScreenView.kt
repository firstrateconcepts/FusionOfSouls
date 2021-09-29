package net.firstrateconcepts.fusionofsouls.view.loading

import com.badlogic.gdx.utils.Align
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.percent
import net.firstrateconcepts.fusionofsouls.util.ext.ui.autoLabel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ScreenView
import kotlin.math.roundToInt

class LoadingScreenView(override val controller: LoadingScreenController, override val vm: LoadingScreenViewModel) : ScreenView() {
    private val logger = fosLogger()

    override fun init() {
        logger.debug { "Initializing" }
        super.init()
        autoLabel({ "Loading ${vm.loadingPercent().percent().roundToInt()}%" }, "title-plain").cell(expand = true, align = Align.center)
    }
}
