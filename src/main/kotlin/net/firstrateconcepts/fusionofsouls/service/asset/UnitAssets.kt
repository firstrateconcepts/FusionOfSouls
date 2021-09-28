package net.firstrateconcepts.fusionofsouls.service.asset

import com.badlogic.gdx.graphics.Texture
import kotlinx.coroutines.Deferred
import ktx.assets.async.AssetStorage
import kotlin.reflect.KMutableProperty0

class UnitAssets(private val assets: AssetStorage) {
    lateinit var heroAsset: Texture
    lateinit var bossAsset: Texture
    lateinit var playerUnitAsset: Texture
    lateinit var enemyUnitAsset: Texture

    fun loadAll(): List<Deferred<Texture>> {
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
}
