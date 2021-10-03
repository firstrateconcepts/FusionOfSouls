package net.firstrateconcepts.fusionofsouls.model.unit

enum class UnitTexture(textureFile: String) {
    HERO("heroArrow-tp.png"),
    BOSS("bossArrow-tp.png"),
    PLAYER("blueArrow-tp.png"),
    ENEMY("redArrow-tp.png");

    val assetFile = "unit/$textureFile"
}
