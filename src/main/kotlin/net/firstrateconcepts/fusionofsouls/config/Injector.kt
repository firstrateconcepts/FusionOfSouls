package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import ktx.inject.Context
import ktx.inject.register
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.service.asset.SkinLoader
import net.firstrateconcepts.fusionofsouls.service.asset.UnitAssets
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreen
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreenController
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuController
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen
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
        bindSingleton<LoadingScreenController>()
        bindSingleton<LoadingScreen>()
        bindSingleton<SettingsDialogController>()
        bindSingleton<MainMenuController>()
        bindSingleton<MainMenuScreen>()
    }
}
