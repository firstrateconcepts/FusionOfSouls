package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar

import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.scene2d.textButton
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindButtonDisabled
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindLabelText
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.TableView

class TopBarView(override val controller: TopBarController, override val vm: TopBarViewModel) : TableView(controller, vm) {
    override fun init() {
        val vm = vm
        val controller = controller

        visTable {
            visLabel("") { bindLabelText { "Gold: ${vm.gold()}" } }.cell(expand = true)
            visLabel("") { bindLabelText { "Units: ${vm.activeUnits()} / ${vm.unitCap()}" } }.cell(expand = true)
        }.cell(expand = true, align = Align.left, padLeft = 5f)

        visTable {
            visLabel("") { bindLabelText { "Room ${vm.floor()}:${vm.room()}" } }.cell(expand = true)

            textButton("Hero") {
                bindButtonDisabled(vm.isDuringBattle, true)
                onChange { controller.heroButtonClicked() }
            }.cell(expand = true)

            textButton("Boss") {
                bindButtonDisabled(vm.isDuringBattle, true)
                onChange { controller.bossButtonClicked() }
            }.cell(expand = true)

            textButton("Menu") {
                onChange { controller.menuButtonClicked() }
            }
        }.cell(expand = true, align = Align.right, padRight = 5f)
    }
}
