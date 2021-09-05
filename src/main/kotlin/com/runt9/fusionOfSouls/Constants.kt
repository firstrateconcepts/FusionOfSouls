package com.runt9.fusionOfSouls

import com.badlogic.gdx.graphics.Color

// Metadata
const val gameTitle = "Fusion of Souls"
val primaryBgColor = Color.valueOf("#2b2b2b")

// Sizes
const val viewportWidth = 640
const val viewportHeight = 360
const val cellSize = 40
const val battleWidth = viewportWidth - (cellSize * 2)
const val battleHeight = viewportHeight - (cellSize * 2)
const val gridWidth = battleWidth / cellSize
const val gridHeight = battleHeight / cellSize
const val basicMargin = 5
const val bigMargin = basicMargin * 2
const val resourceBarHeight = 20
const val gridXStart = ((viewportWidth - battleWidth) / 2)
const val gridYStart = resourceBarHeight + bigMargin

