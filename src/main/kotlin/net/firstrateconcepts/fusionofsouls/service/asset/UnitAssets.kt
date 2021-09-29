package net.firstrateconcepts.fusionofsouls.service.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import kotlinx.coroutines.Deferred
import ktx.assets.async.AssetStorage
import kotlin.reflect.KMutableProperty0

// TODO: Look at what we did for Binding in UI ViewModel classes and use that to "register" assets into an abstract so loading/disposal is a simple loop
class UnitAssets(private val assets: AssetStorage) : Disposable {
    lateinit var heroAsset: Texture
    lateinit var bossAsset: Texture
    lateinit var playerUnitAsset: Texture
    lateinit var enemyUnitAsset: Texture

    fun loadAll(): List<Deferred<*>> {
        return listOf(
            loadAsync("heroArrow-tp.png", this::heroAsset),
            loadAsync("bossArrow-tp.png", this::bossAsset),
            loadAsync("blueArrow-tp.png", this::playerUnitAsset),
            loadAsync("redArrow-tp.png", this::enemyUnitAsset)
        )
    }

    private fun loadAsync(file: String, propToSet: KMutableProperty0<Texture>): Deferred<Texture> {
        val defer = assets.loadAsync<Texture>("unit/$file")
        defer.invokeOnCompletion {
            propToSet.set(defer.getCompleted())
        }
        return defer
    }

    override fun dispose() {
        heroAsset.dispose()
        bossAsset.dispose()
        playerUnitAsset.dispose()
        enemyUnitAsset.dispose()
    }
}
