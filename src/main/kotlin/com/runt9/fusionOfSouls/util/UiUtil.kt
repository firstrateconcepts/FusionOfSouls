package com.runt9.fusionOfSouls.util

import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.vis.KVisTable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
inline fun <S> KWidget<S>.fosVisTable(
    defaultSpacing: Boolean = true,
    init: KVisTable.(S) -> Unit = {}
): KVisTable {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(KVisTable(defaultSpacing)) { s ->
        setFillParent(true)
        defaults()
        this.init(s)
    }
}
