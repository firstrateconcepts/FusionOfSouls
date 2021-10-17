package net.firstrateconcepts.fusionofsouls.service.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.mockk
import net.firstrateconcepts.fusionofsouls.model.RunState
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class EnemyGeneratorTest {
    companion object {
        @JvmStatic
        fun enemyCountArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(1, 1, 1),
            Arguments.of(1, 2, 2),
            Arguments.of(1, 3, 2),
            Arguments.of(1, 4, 2),
            Arguments.of(1, 5, 3),
            Arguments.of(1, 6, 3),
            Arguments.of(1, 7, 3),
            Arguments.of(1, 8, 3),
            Arguments.of(1, 9, 4),
            Arguments.of(1, 10, 4),
            Arguments.of(2, 1, 4),
            Arguments.of(2, 2, 4),
            Arguments.of(2, 3, 4),
            Arguments.of(2, 4, 5),
            Arguments.of(2, 5, 5),
            Arguments.of(2, 6, 5),
            Arguments.of(2, 7, 5),
            Arguments.of(2, 8, 5),
            Arguments.of(2, 9, 5),
            Arguments.of(2, 10, 5),
            Arguments.of(3, 1, 6),
            Arguments.of(3, 2, 6),
            Arguments.of(3, 3, 6),
            Arguments.of(3, 4, 6),
            Arguments.of(3, 5, 6),
            Arguments.of(3, 6, 6),
            Arguments.of(3, 7, 6),
            Arguments.of(3, 8, 6),
            Arguments.of(3, 9, 7),
            Arguments.of(3, 10, 7),
        )

        @JvmStatic
        fun enemyStrengthArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(1, 1, -25),
            Arguments.of(1, 2, -23),
            Arguments.of(1, 3, -21),
            Arguments.of(1, 4, -18),
            Arguments.of(1, 5, -15),
            Arguments.of(1, 6, -11),
            Arguments.of(1, 7, -7),
            Arguments.of(1, 8, -3),
            Arguments.of(1, 9, 1),
            Arguments.of(1, 10, 6),
            Arguments.of(2, 1, 10),
            Arguments.of(2, 2, 16),
            Arguments.of(2, 3, 21),
            Arguments.of(2, 4, 26),
            Arguments.of(2, 5, 32),
            Arguments.of(2, 6, 38),
            Arguments.of(2, 7, 44),
            Arguments.of(2, 8, 50),
            Arguments.of(2, 9, 57),
            Arguments.of(2, 10, 63),
            Arguments.of(3, 1, 70),
            Arguments.of(3, 2, 77),
            Arguments.of(3, 3, 84),
            Arguments.of(3, 4, 92),
            Arguments.of(3, 5, 99),
            Arguments.of(3, 6, 107),
            Arguments.of(3, 7, 114),
            Arguments.of(3, 8, 122),
            Arguments.of(3, 9, 130),
            Arguments.of(3, 10, 138),
        )
    }

    @ParameterizedTest(name = "Room {0}:{1} has {2} enemies")
    @MethodSource("enemyCountArgs")
    fun `Test Enemy Count`(floor: Int, room: Int, count: Int) {
        val runState = RunState(floor = floor, room = room)
        assertThat(EnemyGenerator(mockk(), mockk(), mockk(), mockk()).enemyCount(runState)).isEqualTo(count)
    }

    @ParameterizedTest(name = "Room {0}:{1} enemies have {2}% primary attributes")
    @MethodSource("enemyStrengthArgs")
    fun `Test Enemy Strength`(floor: Int, room: Int, strength: Int) {
        val runState = RunState(floor = floor, room = room)
        assertThat(EnemyGenerator(mockk(), mockk(), mockk(), mockk()).enemyStrength(runState)).isEqualTo(strength)
    }
}
