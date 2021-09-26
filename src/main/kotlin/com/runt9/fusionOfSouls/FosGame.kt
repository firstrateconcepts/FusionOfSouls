package com.runt9.fusionOfSouls

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.kotcrab.vis.ui.VisUI
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.screen.DuringRunScreen
import com.runt9.fusionOfSouls.screen.GdxSteeringTestScreen
import com.runt9.fusionOfSouls.screen.LoadingScreen
import com.runt9.fusionOfSouls.screen.MainMenuScreen
import com.runt9.fusionOfSouls.screen.RunStartScreen
import com.runt9.fusionOfSouls.screen.SettingsDialog
import com.runt9.fusionOfSouls.service.AttackService
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.BattleUnitManager
import com.runt9.fusionOfSouls.service.EnemyGenerator
import com.runt9.fusionOfSouls.service.GridService
import com.runt9.fusionOfSouls.service.PathService
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.service.UnitGenerator
import com.runt9.fusionOfSouls.service.injector
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.view.duringRun.InGameMenuDialog
import com.runt9.fusionOfSouls.view.duringRun.TopBar
import com.runt9.fusionOfSouls.view.duringRun.battleArea.BattleArea
import com.runt9.fusionOfSouls.view.duringRun.battleArea.UnitGridDragPane
import com.runt9.fusionOfSouls.view.duringRun.unitBench.UnitBench
import com.runt9.fusionOfSouls.view.duringRun.unitBench.UnitBenchDragPane
import ktx.actors.stage
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.freetype.registerFreeTypeFontLoaders
import ktx.inject.Context
import ktx.inject.register
import ktx.log.debug
import ktx.scene2d.Scene2DSkin
import kotlin.reflect.jvm.jvmErasure

inline fun <reified Type : Any> Context.autoBindSingleton() = bindSingleton(autoInject<Type>())

inline fun <reified T> autoInject(): T {
    val constructor = T::class.constructors.first()
    val args = constructor.parameters.map { param -> injector.getProvider(param.type.jvmErasure.java)() }.toTypedArray()
    return constructor.call(*args)
}


class FosGame : KtxGame<KtxScreen>() {
    private val profiler by lazy { GLProfiler(Gdx.graphics).apply { enable() } }
    private val stage by lazy {
//        val stage = stage(viewport = FitViewport(viewportWidth.toFloat(), viewportHeight.toFloat()))
//        val stage = stage(viewport = LetterboxingViewport(aspectRatio = 16 / 9f))
        val stage = stage()
        Gdx.input.inputProcessor = stage
        stage
    }


    override fun create() {
        KtxAsync.initiate()
        // TODO: Use real skin
//        VisUI.load(Gdx.files.classpath("ui/uiskin.json"))
        VisUI.load(VisUI.SkinScale.X2)
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        TooltipManager.getInstance().apply {
            instant()
            animations = false
        }

        val assetManager = AssetManager().apply {
            registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
        }

        injector.register {
            bindSingleton { autoInject<PooledEngine>() }
            bindSingleton { stage }
            bindSingleton { assetManager }
            bindSingleton { initSettings(Gdx.app.getPreferences("FusionOfSouls")) }
            bindSingleton { autoInject<GridService>() }
            bindSingleton { UnitGenerator() }
            autoBindSingleton<EnemyGenerator>()
//            bindSingleton { autoInject<EnemyGenerator>() }
            bindSingleton { this@FosGame }

            bindSingleton { autoInject<AttackService>() }
            bind { autoInject<BattleUnitManager>() }
            bindSingleton { autoInject<PathService>() }
            bindSingleton { autoInject<BattleManager>() }

            bind { autoInject<UnitGridDragPane>() }
            bind { autoInject<BattleArea>() }

            bind { autoInject<UnitBenchDragPane>() }
            bind { autoInject<UnitBench>() }

            bind { autoInject<SettingsDialog>() }
            bind { autoInject<InGameMenuDialog>() }

            bind { autoInject<TopBar>() }

            addScreen(autoInject<LoadingScreen>())
            addScreen(autoInject<MainMenuScreen>())
            addScreen(autoInject<RunStartScreen>())
            addScreen(autoInject<DuringRunScreen>())
            addScreen(autoInject<GdxSteeringTestScreen>())
        }

        setScreen<LoadingScreen>()
        super.create()
    }

    private fun initSettings(prefs: Preferences): Settings {
        val settings = Settings(prefs)
        Gdx.graphics.setVSync(settings.vsync)

        if (settings.fullscreen) {
            setFullscreen()
        }

        return settings
    }

    override fun render() {
        profiler.reset()
        super.render()
    }

    override fun dispose() {
        debug { "Draw calls: ${profiler.drawCalls}" }
        debug { "Texture bindings: ${profiler.textureBindings}" }
        debug { "Sprites in batch: ${injector.inject<SpriteBatch>().maxSpritesInBatch}" }

        injector.remove<FosGame>()
        injector.dispose()
        VisUI.dispose()
        super.dispose()
    }

    fun setFullscreen() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        updateViewport()
    }

    fun setWindowed() {
        Gdx.graphics.setWindowedMode(defaultWindowWidth, defaultWindowHeight)
        updateViewport()
    }

    private fun updateViewport() {
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    fun reset() {
        runState = RunState()
        // TODO: This seems like a bad, hacky way to do this instead of properly handling disposal of actors
        removeScreen<DuringRunScreen>()
        injector.apply {
            addScreen(DuringRunScreen(this@FosGame, inject(), inject(), inject(), inject(), inject(), inject()))
            // TODO: Yep also make this not have to reset here
            inject<GridService>().reset()
        }
    }
}
