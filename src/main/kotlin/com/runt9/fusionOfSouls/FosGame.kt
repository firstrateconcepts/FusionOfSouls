package com.runt9.fusionOfSouls

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.screen.DuringRunScene
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
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.inject.register

class FosGame : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        injector.register {
            bindSingleton { Gdx.app.getPreferences("FusionOfSouls") }
            bindSingleton { PooledEngine() }
            bindSingleton { OrthographicCamera().apply { setToOrtho(false, viewportWidth, viewportHeight) } }
            bindSingleton { Settings(inject()) }
            bindSingleton { GridService() }
            bindSingleton { EnemyGenerator(inject()) }

            bindSingleton { RunState() }
            bindSingleton { AttackService(inject()) }
            bind { BattleUnitManager(inject(), inject(), inject()) }
            bind { PathService(inject(), inject()) }
            bindSingleton { BattleManager(inject(), inject(), inject(), inject(), inject()) }

            addScreen(MainMenuScreen(inject()))
            addScreen(RunStartScene(inject()))
            addScreen(DuringRunScene(inject(), inject()))
            addScreen(SettingsScene(inject()))
        }

        setScreen<MainMenuScreen>()
        super.create()
    }

    override fun dispose() {
        injector.dispose()
        super.dispose()
    }
}
