package com.runt9.fusionOfSouls

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object Lwjgl3Launcher {
    private val defaultConfiguration = Lwjgl3ApplicationConfiguration().apply {
        setTitle(gameTitle)
        setWindowedMode(1280, 720)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        setPreferencesConfig("/FusionOfSouls/", Files.FileType.External)
        setInitialBackgroundColor(primaryBgColor)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Lwjgl3Application(FosGame(), defaultConfiguration)
    }
}
