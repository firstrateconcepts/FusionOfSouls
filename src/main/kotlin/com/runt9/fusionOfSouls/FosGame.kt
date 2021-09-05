package com.runt9.fusionOfSouls

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.VisUI
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.screen.DuringRunScene
import com.runt9.fusionOfSouls.screen.LoadingScreen
import com.runt9.fusionOfSouls.screen.MainMenuScreen
import com.runt9.fusionOfSouls.screen.RunStartScene
import com.runt9.fusionOfSouls.screen.SettingsScene
import com.runt9.fusionOfSouls.service.AttackService
import com.runt9.fusionOfSouls.service.BattleManager
import com.runt9.fusionOfSouls.service.BattleUnitManager
import com.runt9.fusionOfSouls.service.EnemyGenerator
import com.runt9.fusionOfSouls.service.GridService
import com.runt9.fusionOfSouls.service.PathService
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.service.injector
import ktx.actors.stage
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.inject.register
import ktx.log.debug
import ktx.scene2d.Scene2DSkin

class FosGame : KtxGame<KtxScreen>() {
    private val profiler by lazy { GLProfiler(Gdx.graphics).apply { enable() } }

    override fun create() {
        KtxAsync.initiate()
        VisUI.load()
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        val stage = stage(viewport = FitViewport(viewportWidth.toFloat(), viewportHeight.toFloat()))
        Gdx.input.inputProcessor = stage

        injector.register {
            bindSingleton { Gdx.app.getPreferences("FusionOfSouls") }
            bindSingleton { PooledEngine() }
            bindSingleton { stage }
            bindSingleton { AssetManager() }
            bindSingleton { Settings(inject()) }
            bindSingleton { GridService() }
            bindSingleton { EnemyGenerator(inject()) }

            bindSingleton { RunState() }
            bindSingleton { AttackService(inject()) }
            bind { BattleUnitManager(inject(), inject(), inject()) }
            bind { PathService(inject(), inject()) }
            bindSingleton { BattleManager(inject(), inject(), inject(), inject(), inject()) }

            addScreen(LoadingScreen(this@FosGame, inject(), inject()))
            addScreen(MainMenuScreen(inject(), inject()))
            addScreen(RunStartScene(inject()))
            addScreen(DuringRunScene(inject(), inject()))
            addScreen(SettingsScene(inject()))
        }

        setScreen<LoadingScreen>()
        super.create()
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
        super.dispose()
    }
}
