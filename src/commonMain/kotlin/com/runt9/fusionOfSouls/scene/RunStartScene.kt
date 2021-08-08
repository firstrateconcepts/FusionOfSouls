package com.runt9.fusionOfSouls.scene

import com.runt9.fusionOfSouls.basicMargin
import com.runt9.fusionOfSouls.model.unit.hero.defaultHero
import com.runt9.fusionOfSouls.service.RunState
import com.runt9.fusionOfSouls.viewportHeight
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.alignTopToBottomOf
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.text
import com.soywiz.korim.text.TextAlignment

class RunStartScene(val runState: RunState) : Scene() {
    override suspend fun Container.sceneInit() {
        text("Choose Hero", 50.0, alignment = TextAlignment.CENTER) {
            centerXOnStage()
            y = (viewportHeight / 5).toDouble()
        }

        val selectButton = uiButton(text = "Select") {
            centerXOnStage()
            y = (viewportHeight / 2).toDouble()

            onClick {
                // TODO: Hero selection
                val hero = defaultHero
                runState.hero = hero

                sceneContainer.changeTo<DuringRunScene>()
            }
        }

        uiButton(text = "Back") {
            centerXOnStage()
            alignTopToBottomOf(selectButton, basicMargin.toDouble())

            onClick {
                sceneContainer.changeTo<MainMenuScene>()
            }
        }
    }
}
