package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Gdx
import ktx.inject.Context
import ktx.inject.register
import net.firstrateconcepts.fusionofsouls.FusionOfSoulsGame
import net.firstrateconcepts.fusionofsouls.service.asset.AssetLoader
import net.firstrateconcepts.fusionofsouls.service.asset.UnitAssets
import net.firstrateconcepts.fusionofsouls.view.loading.LoadingScreen
import net.firstrateconcepts.fusionofsouls.view.mainMenu.MainMenuScreen

inline fun <reified Type : Any> inject(): Type = Injector.inject()

object Injector : Context() {
    fun initStartupDeps() = register {
        bindSingleton<FusionOfSoulsGame>()
        bindSingleton<PlayerSettingsConfig>()
        bindSingleton<ApplicationConfiguration>()
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

    fun initRemainingDeps() = register {
        bindSingleton<AssetConfig>()
        bindSingleton<UnitAssets>()
        bindSingleton<AssetLoader>()
        bindSingleton<ApplicationInitializer>()
        bindSingleton<LoadingScreen>()
        bindSingleton<MainMenuScreen>()
    }
}
