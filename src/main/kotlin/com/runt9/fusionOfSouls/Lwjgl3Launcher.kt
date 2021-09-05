package com.runt9.fusionOfSouls

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object Lwjgl3Launcher {
    private val defaultConfiguration = Lwjgl3ApplicationConfiguration().apply {
        setTitle(gameTitle)
        setWindowedMode(defaultWindowWidth, defaultWindowHeight)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        setResizable(false)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val app = Lwjgl3Application(FosGame(), defaultConfiguration)
        app.logLevel = Application.LOG_DEBUG
    }
}
