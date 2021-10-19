package net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import ktx.scene2d.progressBar
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visTable
import ktx.style.progressBar
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindUpdatable
import net.firstrateconcepts.fusionofsouls.util.ext.ui.rectPixmapTexture
import net.firstrateconcepts.fusionofsouls.util.ext.ui.toDrawable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.GroupView
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class UnitView(override val controller: UnitController, override val vm: UnitViewModel) : GroupView(controller, vm) {
    private val logger = fosLogger()

    override fun init() {
        val vm = vm

        setSize(0.75f, 0.75f)
        setBounds(0f, 0f, 0.75f, 0.75f)
        setOrigin(Align.center)
        bindUpdatable(vm.position) { vm.position.get().apply { setPosition(x, y) } }

        visTable {
            setRound(false)
            setSize(0.75f, 0.3f)
            y = 1f

            fun unitProgressBar(color: Color, binding: ViewModel.Binding<Float>) = progressBar {
                style = this@UnitView.unitBarStyle(color)

                bindUpdatable(binding) { value = binding.get() }

                setAnimateDuration(0.1f)
                setSize(0.75f, 0.1f)
                setOrigin(Align.center)
                setRound(false)
            }.cell(height = 0.06f, width = 0.75f, row = true)

            unitProgressBar(Color.GREEN, vm.hpPercent)
            unitProgressBar(Color.YELLOW, vm.attackTimerPercent)
            unitProgressBar(Color.BLUE, vm.abilityTimerPercent)
        }

        val image = visImage(vm.texture) {
            setSize(0.75f, 0.75f)
            setOrigin(Align.center)
            bindUpdatable(vm.rotation) { vm.rotation.get().apply { rotation = this } }
        }

        bindUpdatable(vm.actorActions) {
            val actions = vm.actorActions.get()
            while (actions.isNotEmpty()) {
                val actionDef = actions.removeFirst()
                if (actionDef.target == ActorActionTarget.IMAGE) {
                    image.addAction(actionDef.action)
                } else {
                    this@UnitView.addAction(actionDef.action)
                }
            }
        }
    }

    private fun unitBarStyle(color: Color) = VisUI.getSkin().progressBar {
        background = rectPixmapTexture(1, 1, Color.DARK_GRAY).toDrawable()
        background.minHeight = 0.05f
        background.minWidth = 0f
        knobBefore = rectPixmapTexture(1, 1, color).toDrawable()
        knobBefore.minHeight = 0.05f
        knobBefore.minWidth = 0f
    }
}
