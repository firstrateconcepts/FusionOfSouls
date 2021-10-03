package net.firstrateconcepts.fusionofsouls.view.heroSelect

import ktx.actors.onChange
import ktx.scene2d.button
import ktx.scene2d.buttonGroup
import ktx.scene2d.textButton
import ktx.scene2d.textField
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.ScreenView

class HeroSelectView(override val controller: HeroSelectScreenController, override val vm: HeroSelectViewModel) : ScreenView(controller, vm) {
    override fun init() {
        super.init()
        val vm = vm
        val controller = controller

        visLabel("Select Hero", "title-plain").cell(row = true, spaceBottom = 5f)

        vm.heroDefs.forEach { hero ->
            visTable {
                buttonGroup(1, 1) {
                    button(style = "toggle") {
                        visLabel(hero.name).cell(row = true)
                        visImage(hero.texture)

                        onChange { vm.selectedHero = hero }
                    }
                }
            }.cell(space = 5f)
        }

        row()

        // TODO: Probably some max length validation or something
        textField {
            messageText = "Enter Seed?"

            onChange { vm.seed(text) }
        }.cell(row = true, spaceBottom = 5f)

        textButton("Start", "round") { onChange { controller.startRun() } }.cell(row = true, spaceBottom = 5f)
        textButton("Back", "round") { onChange { controller.back() } }
    }
}
