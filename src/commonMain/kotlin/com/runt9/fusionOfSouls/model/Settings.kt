package com.runt9.fusionOfSouls.model

import com.soywiz.korge.service.storage.IStorage
import kotlin.reflect.KProperty

const val fullscreenSetting = "fullscreen"
const val vsyncSetting = "vsync"
const val defaultFullscreen = false
const val defaultVsync = true

class Settings(private val storage: IStorage) {
    var fullscreen: Boolean by SettingDelegate(fullscreenSetting, defaultFullscreen, String::toBoolean)
    var vsync: Boolean by SettingDelegate(vsyncSetting, defaultVsync, String::toBoolean)

    private fun <T> getSettingOrDefault(setting: String, defaultValue: T, parser: String.() -> T) = storage.getOrNull(setting)?.parser() ?: defaultValue

    inner class SettingDelegate<T>(private val setting: String, private val default: T, private val parser: String.() -> T) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return getSettingOrDefault(setting, default, parser)
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            storage[setting] = value.toString()
        }
    }
}
