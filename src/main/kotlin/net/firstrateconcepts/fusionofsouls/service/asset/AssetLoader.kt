package net.firstrateconcepts.fusionofsouls.service.asset

import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import ktx.async.KtxAsync

class AssetLoader(private val unitAssets: UnitAssets) {
    fun load() = KtxAsync.async {
        unitAssets.loadAll().joinAll()
    }
}
