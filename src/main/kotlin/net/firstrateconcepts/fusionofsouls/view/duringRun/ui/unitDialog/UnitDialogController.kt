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
import net.firstrateconcepts.fusionofsouls.model.component.unit.classes
import net.firstrateconcepts.fusionofsouls.model.component.unit.fusions
import net.firstrateconcepts.fusionofsouls.model.component.unit.runes
import net.firstrateconcepts.fusionofsouls.model.component.unit.xp
import net.firstrateconcepts.fusionofsouls.model.loot.Rune
import net.firstrateconcepts.fusionofsouls.model.unit.hero.getHeroDefinitionForId
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.service.unit.RuneService
import net.firstrateconcepts.fusionofsouls.util.ext.withHero
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.DialogController

class UnitDialogController(
    private val graphics: Graphics,
    private val engine: AsyncPooledEngine,
    private val assets: AssetStorage,
    private val runStateService: RunStateService,
    private val runeService: RuneService
) : DialogController() {
    override lateinit var vm: UnitDialogViewModel
    override lateinit var view: UnitDialogView

    override fun load() {
        val runState = runStateService.load()
        val passive = getHeroDefinitionForId(runState.selectedHeroId)!!.passive

        runBlocking {
            engine.withHero {
                vm = UnitDialogViewModel(it.id, it.name, it.texture, passive)
                vm.attrs(it.attrs.attrs.values)
                vm.abilityName(it.ability.name)
                vm.abilityDescription(it.ability.description)
                vm.abilityBaseCooldown(it.ability.cooldown)
                vm.abilityCurrentCooldown(it.ability.cooldown / it.attrs.cooldownReduction())
                vm.runes(it.runes)
                vm.runeCap(runState.runeCap)
                vm.fusionCap(runState.fusionCap)
                vm.classes(it.classes)

                it.xp.apply {
                    vm.xp(xp)
                    vm.xpToLevel(xpToLevel)
                    vm.level(level)
                }

                it.fusions.apply {
                    vm.passives(passives)
                    vm.attrMods(attrMods)
                    vm.abilityAugs(abilityAugs)
                    vm.synergies(synergies)
                    vm.fusionCount(fusionCount)
                }
            }.join()
        }

        view = UnitDialogView(this, vm, assets, graphics.width, graphics.height)
    }

    fun runeClick(rune: Rune) {
        runBlocking {
            engine.withHero {
                if (rune.active) {
                    runeService.activateRune(it, rune)
                } else {
                    runeService.deactivateRune(it, rune)
                }

                vm.runes(it.runes)
            }
        }
    }
}
