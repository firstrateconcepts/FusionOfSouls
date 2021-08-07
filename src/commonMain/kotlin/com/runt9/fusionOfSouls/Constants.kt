package com.runt9.fusionOfSouls

import com.soywiz.korim.color.Colors

// Metadata
const val gameTitle = "Fusion of Souls"
val primaryBgColor = Colors["#2b2b2b"]

// Sizes
const val viewportWidth = 640
const val viewportHeight = 360
const val cellSize = 40
const val battleWidth = viewportWidth - (cellSize * 2)
const val battleHeight = viewportHeight - (cellSize * 2)
const val gridWidth = battleWidth / cellSize
const val gridHeight = battleHeight / cellSize
const val basicMargin = 5

