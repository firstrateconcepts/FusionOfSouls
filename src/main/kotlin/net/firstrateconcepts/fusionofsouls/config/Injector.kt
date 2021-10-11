package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.GdxAI
import ktx.inject.Context
import ktx.inject.register
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.service.asset.SkinLoader
import net.firstrateconcepts.fusionofsouls.service.duringRun.AttributeCalculator
import net.firstrateconcepts.fusionofsouls.service.duringRun.BattleManager
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunInitializer
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunStateService
import net.firstrateconcepts.fusionofsouls.service.system.AbilitySystem
import net.firstrateconcepts.fusionofsouls.service.system.AttackingSystem
import net.firstrateconcepts.fusionofsouls.service.system.SteeringSystem
import net.firstrateconcepts.fusionofsouls.service.system.TargetingSystem
import net.firstrateconcepts.fusionofsouls.service.system.TimersSystem
import net.firstrateconcepts.fusionofsouls.service.unit.AttackService
import net.firstrateconcepts.fusionofsouls.service.unit.EnemyGenerator
import net.firstrateconcepts.fusionofsouls.service.unit.UnitManager
import net.firstrateconcepts.fusionofsouls.service.unit.action.ActionQueueBus
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.util.framework.ui.DialogManager
import net.firstrateconcepts.fusionofsouls.view.duringRun.DuringRunScreen
import net.firstrateconcepts.fusionofsouls.view.duringRun.game.DuringRunGameController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.DuringRunUiController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.menu.MenuDialogController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.topBar.TopBarController
import net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitBench.UnitBenchController
import net.firstrateconcepts.fusionofsouls.view.heroSelect.HeroSelectScreenController
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreenController
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreenController
import net.firstrateconcepts.fusionofsouls.view.settings.SettingsDialogController

inline fun <reified Type : Any> inject(): Type = Injector.inject()
inline fun <reified Type : Any> lazyInject() = lazy { inject<Type>() }

object Injector : Context() {
    fun initStartupDeps() = register {
        bindSingleton<FusionOfSoulsGame>()
        bindSingleton<PlayerSettingsConfig>()
        bindSingleton<ApplicationConfiguration>()
        bindSingleton<EventBus>()
        bindSingleton<AssetConfig>()
        bindSingleton<SkinLoader>()
        bindSingleton<AssetLoader>()
        bindSingleton<ApplicationInitializer>()
    }

    fun initGdxDeps() = register {
        bindSingleton(Gdx.app)
        bindSingleton(Gdx.audio)
        bindSingleton(Gdx.files)
        bindSingleton(Gdx.gl)
        bindSingleton(Gdx.graphics)
        bindSingleton(Gdx.input)
        bindSingleton(Gdx.net)
    }

    fun initRunningDeps() = register {
        bindSingleton(InputMultiplexer())
        bindSingleton(GdxAI.getTimepiece())

        bindSingleton<AsyncPooledEngine>()
        bindSingleton<UnitManager>()
        bindSingleton<RunServiceRegistry>()
        bindSingleton<RunStateService>()
        bindSingleton<ActionQueueBus>()
        bindSingleton<AttackService>()

        bindSingleton<SteeringSystem>()
        bindSingleton<TargetingSystem>()
        bindSingleton<AttackingSystem>()
        bindSingleton<TimersSystem>()
        bindSingleton<AbilitySystem>()

        bindSingleton<AttributeCalculator>()
        bindSingleton<RandomizerService>()
        bindSingleton<RunInitializer>()
        bindSingleton<EnemyGenerator>()
        bindSingleton<BattleManager>()

        bindSingleton<DialogManager>()
        bindSingleton<LoadingScreenController>()
        bindSingleton<MainMenuScreenController>()
        bindSingleton<HeroSelectScreenController>()
        bindSingleton<SettingsDialogController>()
        bindSingleton<DuringRunUiController>()
        bindSingleton<DuringRunGameController>()
        bindSingleton<DuringRunScreen>()
        bindSingleton<TopBarController>()
        bindSingleton<UnitBenchController>()
        bindSingleton<MenuDialogController>()
    }
}
