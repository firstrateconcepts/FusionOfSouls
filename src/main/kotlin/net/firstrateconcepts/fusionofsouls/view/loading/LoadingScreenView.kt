package net.firstrateconcepts.fusionofsouls.view.loading

import ktx.scene2d.scene2d
import ktx.scene2d.vis.visLabel

class LoadingScreenView(private val controller: LoadingScreenController) {
    val viewDefinition = scene2d.visLabel("Loading...")
}
