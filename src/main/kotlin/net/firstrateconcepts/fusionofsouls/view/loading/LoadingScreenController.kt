package net.firstrateconcepts.fusionofsouls.view.loading

import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Controller

class LoadingScreenController(private val assets: AssetStorage) : Controller {
    private val logger = fosLogger()
    override val vm = LoadingScreenViewModel()
    override val view = LoadingScreenView(this, vm)

    override fun render(delta: Float) {
        assets.progress.run {
            logger.debug { "Asset loading status: $loaded / $total (${percent * 100}%)" }
            vm.loadingPercent(percent)
        }
    }
}
