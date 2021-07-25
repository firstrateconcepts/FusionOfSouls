package com.runt9.fusionOfSouls
import com.soywiz.korge.scene.Module
import com.soywiz.korge.service.storage.storage
import com.soywiz.korge.view.views
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.scene.BattleScene
import com.runt9.fusionOfSouls.scene.MainMenuScene
import com.runt9.fusionOfSouls.scene.SettingsScene

object MainModule : Module() {
    override val title = gameTitle
    // TODO: Get from settings
    override val windowSize = SizeInt(1280, 720)
    override val size = SizeInt(virtualWidth, virtualHeight)
    override val bgcolor = primaryBgColor

    override val mainScene = MainMenuScene::class

    override suspend fun AsyncInjector.configure() {
        mapInstance(Settings(views().storage))
        mapPrototype { MainMenuScene(get()) }
        mapPrototype { BattleScene() }
        mapPrototype { SettingsScene(get()) }
    }
}
