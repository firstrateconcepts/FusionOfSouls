package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog

import com.badlogic.gdx.Graphics
import kotlinx.coroutines.runBlocking
import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.cooldownReduction
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.texture
import net.firstrateconcepts.fusionofsouls.model.component.unit.ability
import net.firstrateconcepts.fusionofsouls.model.component.unit.runes
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.util.ext.withHero
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController

class UnitDialogController(
    private val graphics: Graphics,
    private val engine: AsyncPooledEngine,
    private val assets: AssetStorage,
    private val runStateService: RunStateService
) : DialogController() {
    override lateinit var vm: UnitDialogViewModel
    override lateinit var view: UnitDialogView

    override fun load() {
        runBlocking {
            engine.withHero {
                vm = UnitDialogViewModel(it.id, it.name, it.texture)
                vm.attrs(it.attrs.attrs.values)
                vm.abilityName(it.ability.name)
                vm.abilityDescription(it.ability.description)
                vm.abilityBaseCooldown(it.ability.cooldown)
                vm.abilityCurrentCooldown(it.ability.cooldown / it.attrs.cooldownReduction())
                vm.runes(it.runes)
                vm.runeCap(runStateService.load().runeCap)

            }.join()
        }

        view = UnitDialogView(this, vm, assets, graphics.width, graphics.height)
    }

    // TODO: Deal with binding to changed attributes
}
