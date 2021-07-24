package scene
import basicMargin
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.alignTopToBottomOf
import com.soywiz.korge.view.centerXOnStage
import com.soywiz.korge.view.text
import com.soywiz.korim.text.TextAlignment
import virtualHeight

class MainMenu : Scene() {
    override suspend fun Container.sceneInit() {
        text("Fusion of Souls", 50.0, alignment = TextAlignment.CENTER) {
            centerXOnStage()
            y = (virtualHeight / 5).toDouble()
        }

        val startRunButton = uiButton(text = "Start Run") {
            centerXOnStage()
            y = (virtualHeight / 2).toDouble()

            onClick {
                sceneContainer.changeTo<Battle>()
            }
        }

        val settingsButton = uiButton(text = "Settings") {
            centerXOnStage()
            alignTopToBottomOf(startRunButton, basicMargin.toDouble())

            onClick {
                sceneContainer.changeTo<Settings>()
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
