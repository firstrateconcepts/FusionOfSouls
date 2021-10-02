package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.gdx.utils.Disposable

interface RunService : Disposable {
    fun start() = Unit
    fun frame(delta: Float) = Unit
    fun stop() = Unit
    override fun dispose() = Unit
}
