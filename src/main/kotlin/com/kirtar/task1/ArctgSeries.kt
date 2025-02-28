package com.kirtar.task1

object ArctgSeries {
    /**
     * Вычисляет arctg(x) с использованием разложения в степенной ряд.
     * @param x значение аргумента (в радианах)
     * @param n количество членов ряда
     * @return приближённое значение arctg(x)
     */
    fun arctg(x: Double, n: Int): Double {
        var sum = 0.0
        for (i in 0 until n) {
            val power = 2 * i + 1
            // (-1)^i задаёт знак члена
            val sign = if (i % 2 == 0) 1.0 else -1.0
            sum += sign * Math.pow(x, power.toDouble()) / power
        }
        return sum
    }
}
