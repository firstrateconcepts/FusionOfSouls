
import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import scene.Battle
import scene.MainMenu
import scene.Settings

object MainModule : Module() {
    override val title = gameTitle
    // TODO: Get from settings
    override val windowSize = SizeInt(1280, 720)
    override val size = SizeInt(virtualWidth, virtualHeight)
    override val bgcolor = primaryBgColor

    override val mainScene = MainMenu::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { MainMenu() }
        mapPrototype { Battle() }
        mapPrototype { Settings() }
    }
}
