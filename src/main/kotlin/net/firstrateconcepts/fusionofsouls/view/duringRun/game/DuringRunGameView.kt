package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import ktx.actors.centerPosition
import ktx.scene2d.vis.KFloatingGroup
import ktx.scene2d.vis.floatingGroup
import ktx.scene2d.vis.visImage
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.TableView
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_HEIGHT
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_HEIGHT_MARGIN
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_WIDTH
import net.firstrateconcepts.fusionofsouls.view.duringRun.GAME_WIDTH_MARGIN

class DuringRunGameView(override val controller: DuringRunGameController, override val vm: DuringRunGameViewModel) : TableView(controller, vm) {
    private var unitPanel: KFloatingGroup? = null

    override fun init() {
        val vm = vm
        setSize(GAME_WIDTH - GAME_WIDTH_MARGIN, GAME_HEIGHT - GAME_HEIGHT_MARGIN)
        centerPosition()

        unitPanel = floatingGroup {
            vm.units.bind {
                clear()
                vm.units.get().forEach { unit ->
                    // TODO: This will end up needing to be its own UI component since it'll also have health bar/cooldown bar
                    visImage(unit.texture) {
                        setSize(1f, 1f)
                        unit.position.bind {
                            val position = unit.position.get()
                            this@visImage.setPosition(position.x, position.y)
                        }
                    }
                }
            }
        }.cell(row = true, grow = true)
    }

    override fun dispose() {
        unitPanel = null
        super.dispose()
    }
}