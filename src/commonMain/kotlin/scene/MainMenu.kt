package scene
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiVerticalStack
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.centerOnStage

class MainMenu : Scene() {
    override suspend fun Container.sceneInit() {
        uiVerticalStack {
            centerOnStage()

            uiButton(text = "Start Run") {
                onClick {
                    sceneContainer.changeTo<Battle>()
                }
            }

            uiButton(text = "Settings") {
                onClick {
                    sceneContainer.changeTo<Settings>()
                }
            }

            uiButton(text = "Quit") {
                onClick {
                    views.closeSuspend()
                }
            }
        }

    }
}
