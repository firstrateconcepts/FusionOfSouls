package net.firstrateconcepts.fusionofsouls.view.heroSelect

import ktx.assets.async.AssetStorage
import net.firstrateconcepts.fusionofsouls.model.RunState
import net.firstrateconcepts.fusionofsouls.model.event.enqueueChangeScreen
import net.firstrateconcepts.fusionofsouls.model.unit.hero.DefaultHeroDefinition
import net.firstrateconcepts.fusionofsouls.model.unit.hero.HeroDefinition
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.util.ext.randomString
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.controller.UiScreenController
import net.firstrateconcepts.fusionofsouls.view.duringRun.DuringRunScreen
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController
import kotlin.random.Random

class HeroSelectScreenController(
    private val eventBus: EventBus,
    private val runStateService: RunStateService,
    private val assets: AssetStorage
) : UiScreenController() {
    override val vm = HeroSelectViewModel()
    override val view = HeroSelectView(this, vm)

    override fun load() {
        vm.heroDefs = HeroDefinition::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
            .map { HeroSelectViewModel.HeroDefinitionDto(it.id, it.name, assets[it.texture.assetFile]) }
    }

    fun back() = eventBus.enqueueChangeScreen<MainMenuScreenController>()
    fun startRun() {
        eventBus.enqueueChangeScreen<DuringRunScreen>()
        val heroId = vm.selectedHero?.id ?: DefaultHeroDefinition.id
        val seed = vm.seed.get().ifBlank { Random.randomString(8) }
        runStateService.save(RunState(seed = seed, selectedHeroId = heroId))
    }
}
