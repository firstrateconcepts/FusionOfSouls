package net.firstrateconcepts.fusionofsouls.config

import ktx.assets.async.AssetStorage
import ktx.freetype.async.registerFreeTypeFontLoaders

class AssetConfig {
    val assetStorage: AssetStorage

    init {
        assetStorage = configureAssetStorage()
        Injector.bindSingleton(assetStorage)
    }

    private fun configureAssetStorage() = AssetStorage().apply {
        registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
    }

    // TODO: Maybe a better way to do this as the list of assets grows
    suspend fun loadAssets() {

    }
}
