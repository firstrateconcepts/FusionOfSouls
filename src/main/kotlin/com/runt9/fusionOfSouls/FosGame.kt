package com.runt9.fusionOfSouls

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.VisUI
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.screen.LoadingScreen
import com.runt9.fusionOfSouls.screen.MainMenuScreen
import com.runt9.fusionOfSouls.screen.RunStartScreen
import com.runt9.fusionOfSouls.screen.SettingsScreen
import com.runt9.fusionOfSouls.screen.duringRun.DuringRunScreen
import com.runt9.fusionOfSouls.service.AttackService
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.BattleUnitManager
import com.runt9.fusionOfSouls.service.EnemyGenerator
import com.runt9.fusionOfSouls.service.GridService
import com.runt9.fusionOfSouls.service.PathService
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.service.injector
import com.runt9.fusionOfSouls.service.runState
import ktx.actors.stage
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.inject.register
import ktx.log.debug
import ktx.scene2d.Scene2DSkin

class FosGame : KtxGame<KtxScreen>() {
    private val profiler by lazy { GLProfiler(Gdx.graphics).apply { enable() } }
    private val stage by lazy {
        val stage = stage(viewport = FitViewport(viewportWidth.toFloat(), viewportHeight.toFloat()))
        Gdx.input.inputProcessor = stage
        stage
    }

    override fun create() {
        KtxAsync.initiate()
        // TODO: Use real skin
//        VisUI.load(Gdx.files.classpath("ui/uiskin.json"))
        VisUI.load()
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        TooltipManager.getInstance().apply {
            instant()
            animations = false
        }

        injector.register {
            bindSingleton { PooledEngine() }
            bindSingleton { stage }
            bindSingleton { AssetManager() }
            bindSingleton { initSettings(Gdx.app.getPreferences("FusionOfSouls")) }
            bindSingleton { GridService() }
            bindSingleton { EnemyGenerator(inject()) }

            bindSingleton { AttackService() }
            bind { BattleUnitManager(inject(), inject()) }
            bind { PathService(inject(), inject()) }
            bindSingleton { BattleManager(inject(), inject(), inject(), inject()) }

            addScreen(LoadingScreen(this@FosGame, inject(), inject()))
            addScreen(MainMenuScreen(this@FosGame, inject(), inject()))
            addScreen(RunStartScreen(this@FosGame, inject()))
            addScreen(DuringRunScreen(this@FosGame, inject(), inject()))
            addScreen(SettingsScreen(this@FosGame, inject(), inject()))
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
    }
}
