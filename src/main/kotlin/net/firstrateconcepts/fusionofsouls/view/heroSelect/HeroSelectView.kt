package net.firstrateconcepts.fusionofsouls.view.heroSelect

import ktx.actors.onChange
import ktx.scene2d.textButton
import ktx.scene2d.vis.visLabel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.ViewModel
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.ScreenView

class HeroSelectView(override val controller: HeroSelectScreenController, override val vm: ViewModel) : ScreenView(controller, vm) {
    override fun init() {
        super.init()

        visLabel("Select Hero", "title-plain").cell(row = true)
        textButton("Start", "round") { onChange { this@HeroSelectView.controller.startRun() } }.cell(row = true)
        textButton("Back", "round") { onChange { this@HeroSelectView.controller.back() } }
    }
}
