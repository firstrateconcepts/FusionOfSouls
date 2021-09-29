package net.firstrateconcepts.fusionofsouls.view.loading

import com.badlogic.gdx.utils.Align
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.ui.autoLabel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ScreenView

class LoadingScreenView(override val controller: LoadingScreenController, override val vm: LoadingScreenViewModel) : ScreenView() {
    private val logger = fosLogger()

    override fun init() {
        logger.debug { "Initializing" }
        super.init()
        autoLabel({ "Loading ${vm.loadingPercent() * 100}%" }).cell(expand = true, align = Align.center)
    }

    override fun dispose() {
        logger.debug { "Disposing" }
        clear()
        remove()
    }
}
