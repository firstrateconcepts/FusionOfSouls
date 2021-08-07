package com.runt9.fusionOfSouls.scene

import com.runt9.fusionOfSouls.basicMargin
import com.runt9.fusionOfSouls.cellSize
import com.runt9.fusionOfSouls.model.Settings
import com.runt9.fusionOfSouls.viewportHeight
import com.runt9.fusionOfSouls.viewportWidth
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiCheckBox
import com.soywiz.korge.ui.uiContainer
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alignTopToBottomOf
import com.soywiz.korge.view.centerOn
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.centerXOn
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.text
import com.soywiz.korim.text.TextAlignment

class SettingsScene(private val settings: Settings) : Scene() {
    override suspend fun Container.sceneInit() {
        val window = views.gameWindow

        uiContainer {
            scaledHeight = viewportHeight.toDouble()
            scaledWidth = (viewportWidth * (2 / 3)).toDouble()

            centerOnStage()

            text("Settings", textSize = cellSize.toDouble(), alignment = TextAlignment.CENTER) {
                centerXOnStage()
                y = (viewportHeight / 5).toDouble()
            }

            val fullScreenCheckbox = uiCheckBox(text = "Fullscreen", checked = window.fullscreen) {
                centerOn(this@uiContainer)

                onChange.add {
                    window.fullscreen = it.checked
                }
            }

            uiCheckBox(text = "VSync", checked = window.vsync) {
                centerXOn(this@uiContainer)
                alignTopToBottomOf(fullScreenCheckbox, padding = basicMargin)

                onChange.add {
                    window.vsync = it.checked
                }
            }

            uiButton(text = "Done") {
                centerXOn(this@uiContainer)
                alignBottomToBottomOf(this@uiContainer, padding = basicMargin)

                onClick {
                    settings.fullscreen = window.fullscreen
                    settings.vsync = window.vsync
                    sceneContainer.changeTo<MainMenuScene>()
                }
            }
        }
    }
}
