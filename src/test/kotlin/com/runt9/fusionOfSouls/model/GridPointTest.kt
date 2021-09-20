package com.runt9.fusionOfSouls.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GridPointTest {
    @ParameterizedTest(name = "Expect index [{0}] to equal ({1}, {2})")
    @MethodSource
    fun `Test fromUserGridCellIndex`(cellIndex: Int, expectedX: Int, expectedY: Int) {
        assertThat(GridPoint.fromUserGridCellIndex(cellIndex)).isEqualTo(GridPoint(expectedX, expectedY))
    }

    companion object {
        @JvmStatic
        fun `Test fromUserGridCellIndex`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 6),
                Arguments.of(1, 1, 6),
                Arguments.of(2, 2, 6),
                Arguments.of(3, 3, 6),
                Arguments.of(4, 0, 5),
                Arguments.of(5, 1, 5),
                Arguments.of(6, 2, 5),
                Arguments.of(7, 3, 5),
                Arguments.of(8, 0, 4),
                Arguments.of(9, 1, 4),
                Arguments.of(10, 2, 4),
                Arguments.of(11, 3, 4),
                Arguments.of(12, 0, 3),
                Arguments.of(13, 1, 3),
                Arguments.of(14, 2, 3),
                Arguments.of(15, 3, 3),
                Arguments.of(16, 0, 2),
                Arguments.of(17, 1, 2),
                Arguments.of(18, 2, 2),
                Arguments.of(19, 3, 2),
                Arguments.of(20, 0, 1),
                Arguments.of(21, 1, 1),
                Arguments.of(22, 2, 1),
                Arguments.of(23, 3, 1),
                Arguments.of(24, 0, 0),
                Arguments.of(25, 1, 0),
                Arguments.of(26, 2, 0),
                Arguments.of(27, 3, 0),
            )
        }
    }
}
