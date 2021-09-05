package com.runt9.fusionOfSouls.model

import com.badlogic.gdx.Preferences
import ktx.preferences.get
import ktx.preferences.set
import kotlin.reflect.KProperty

const val fullscreenSetting = "fullscreen"
const val vsyncSetting = "vsync"
const val defaultFullscreen = false
const val defaultVsync = true

class Settings(private val prefs: Preferences) {
    var fullscreen: Boolean by SettingDelegate(fullscreenSetting, defaultFullscreen)
    var vsync: Boolean by SettingDelegate(vsyncSetting, defaultVsync)

    @Suppress("UNCHECKED_CAST")
    inner class SettingDelegate<T>(private val setting: String, private val default: Any) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return prefs[setting, default] as T
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
            prefs[setting] = value
        }
    }
}
