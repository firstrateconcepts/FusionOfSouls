package net.firstrateconcepts.fusionofsouls.model

import kotlinx.serialization.Serializable
import net.firstrateconcepts.fusionofsouls.util.ext.randomString
import kotlin.random.Random

@Serializable
data class RunState(
    val seed: String = Random.randomString(8),
    val selectedHeroId: Int = 1,
    var gold: Int = 0,
    var floor: Int = 1,
    var room: Int = 1,
    var unitCap: Int = 3,
    var runeCap: Int = 2,
    var fusionCap: Int = 5,
    var status: RunStatus = RunStatus.BEFORE_BATTLE
)

enum class RunStatus { PAUSED, BEFORE_BATTLE, AFTER_BATTLE, DURING_BATTLE }
