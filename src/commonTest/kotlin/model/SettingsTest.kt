package model

import com.soywiz.korge.service.storage.InmemoryStorage
import com.soywiz.korge.service.storage.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsTest {
    private val storage = InmemoryStorage()
    private val settings = Settings(storage)

    @Test
    fun `Test Fullscreen - Get default`() {
        assertEquals(defaultFullscreen, settings.fullscreen)
    }
    
    @Test
    fun `Test Fullscreen - Get false`() {
        storage[fullscreenSetting] = "false"
        assertFalse(settings.fullscreen)
    }
    
    @Test
    fun `Test Fullscreen - Get true`() {
        storage[fullscreenSetting] = "true"
        assertTrue(settings.fullscreen)
    }
    
    @Test
    fun `Test Fullscreen - Set false`() {
        settings.fullscreen = false
        assertEquals("false", storage[fullscreenSetting])
    }
    
    @Test
    fun `Test Fullscreen - Set true`() {
        settings.fullscreen = true
        assertEquals("true", storage[fullscreenSetting])
    }

    @Test
    fun `Test Vsync - Get default`() {
        assertEquals(defaultVsync, settings.vsync)
    }
    
    @Test
    fun `Test Vsync - Get false`() {
        storage[vsyncSetting] = "false"
        assertFalse(settings.vsync)
    }
    
    @Test
    fun `Test Vsync - Get true`() {
        storage[vsyncSetting] = "true"
        assertTrue(settings.vsync)
    }

    @Test
    fun `Test Vsync - Set false`() {
        settings.vsync = false
        assertEquals("false", storage[vsyncSetting])
    }

    @Test
    fun `Test Vsync - Set true`() {
        settings.vsync = true
        assertEquals("true", storage[vsyncSetting])
    }
}
