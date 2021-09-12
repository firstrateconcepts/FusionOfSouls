package com.runt9.fusionOfSouls.model

import com.badlogic.gdx.Preferences
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set

const val fullscreenSetting = "fullscreen"
const val vsyncSetting = "vsync"
const val defaultFullscreen = false
const val defaultVsync = true

class Settings(private val prefs: Preferences) {
    var fullscreen = prefs[fullscreenSetting, defaultFullscreen]
    var vsync = prefs[vsyncSetting, defaultVsync]

    fun save() {
        prefs.flush {
            this[fullscreenSetting] = fullscreen
            this[vsyncSetting] = vsync
        }
    }
}
