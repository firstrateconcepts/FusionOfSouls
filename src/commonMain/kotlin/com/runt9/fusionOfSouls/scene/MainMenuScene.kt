package com.runt9.fusionOfSouls.scene
import com.runt9.fusionOfSouls.basicMargin
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.viewportHeight
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.alignTopToBottomOf
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.text
import com.soywiz.korim.text.TextAlignment

class MainMenuScene(private val settings: Settings) : Scene() {
    override suspend fun Container.sceneInit() {
        views.gameWindow.fullscreen = settings.fullscreen
        views.gameWindow.vsync = settings.vsync

        text("Fusion of Souls", 50.0, alignment = TextAlignment.CENTER) {
            centerXOnStage()
            y = (viewportHeight / 5).toDouble()
        }

        val startRunButton = uiButton(text = "New Run") {
            centerXOnStage()
            y = (viewportHeight / 2).toDouble()

            onClick {
                sceneContainer.changeTo<RunStartScene>()
            }
        }

        val settingsButton = uiButton(text = "Settings") {
            centerXOnStage()
            alignTopToBottomOf(startRunButton, basicMargin.toDouble())

            onClick {
                sceneContainer.changeTo<SettingsScene>()
            }
        }

        uiButton(text = "Quit") {
            centerXOnStage()
            alignTopToBottomOf(settingsButton, basicMargin.toDouble())

            onClick {
                views.closeSuspend()
            }
        }
    }
}
