package net.firstrateconcepts.fusionofsouls.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.kotcrab.vis.ui.VisUI
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin

class ApplicationInitializer {
    fun initialize() {
        KtxAsync.initiate()
        VisUI.load(Gdx.files.classpath("skin/star-soldier-ui.json"))
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        TooltipManager.getInstance().apply {
            instant()
            animations = false
        }
    }
}
