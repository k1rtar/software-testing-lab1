package com.kirtar.task1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.atan

class ArctgSeriesTest {
    @ParameterizedTest
    @CsvSource(
        "0.0, 10",
        "0.5, 15",
        "0.9, 20",
        "-0.5, 15"
    )
    fun `test arctg series approximation`(x: Double, terms: Int) {
        val expected = atan(x)
        val result = ArctgSeries.arctg(x, terms)
        assertEquals(expected, result, 1e-3)
    }
}
