package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import ktx.inject.Context
import ktx.inject.register
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.service.RunInitializer
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.service.asset.SkinLoader
import net.firstrateconcepts.fusionofsouls.service.asset.UnitAssets
import net.firstrateconcepts.fusionofsouls.service.entity.UnitBuilder
import net.firstrateconcepts.fusionofsouls.service.system.MovementSystem
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
        bindSingleton<UnitAssets>()
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
        bindSingleton { InputMultiplexer() }
        bindSingleton<DialogManager>()
        bindSingleton<LoadingScreenController>()
        bindSingleton<MainMenuScreenController>()
        bindSingleton<HeroSelectScreenController>()
        bindSingleton<SettingsDialogController>()

        bindSingleton(PooledEngine())
        bindSingleton<UnitBuilder>()
        bindSingleton<MovementSystem>()

        bindSingleton<RunInitializer>()
        bindSingleton<DuringRunUiController>()
        bindSingleton<DuringRunGameController>()
        bindSingleton<DuringRunScreen>()
        bindSingleton<TopBarController>()
        bindSingleton<UnitBenchController>()
        bindSingleton<MenuDialogController>()
    }
}
