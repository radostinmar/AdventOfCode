package y2022

import Day
import kotlin.math.pow

// 9 - 2- - 2 * 5 + 1 * -1
// 10 - 20 - 2 * 5 + 1 * 0
// 11 - 21 - 2 * 5 + 1 * 1
// 12 - 21 - 2 * 5 + 1 * 2
// 13 - 1== - 1 * 25 + -2 * 5 + 1 * - 2 / 1==
// 14 - 1=- - 1 * 25 + -2 * 5 + 1 * -1
// 15 - 1=0 - 1 * 25 + -2 * 5 + 1 * -1
// 33 - 113

object Day25 : Day(isTest = false, year = 2022) {

    private val charMap = mapOf(
        '2' to 2,
        '1' to 1,
        '0' to 0,
        '-' to -1,
        '=' to -2
    )

    private val snafuMap = mapOf(
        5 to '0',
        4 to '-',
        3 to '=',
        2 to '2',
        1 to '1',
        0 to '0'
    )

    private fun toDecimal(number: String): Long =
        number.reversed().withIndex().sumOf {
            5.0.pow(it.index).toLong() * charMap.getValue(it.value)
        }

    private fun toSnafu(number: Long): String {
        var num = number
        var snafu = ""
        var plusOne = false
        while (num > 0) {
            val value = (num % 5).toInt().let { if (plusOne) it + 1 else it }
            plusOne = value > 2
            snafu = snafuMap.getValue(value) + snafu
            num /= 5
        }
        return snafu
    }

    override fun part1(): String =
        lines.sumOf(::toDecimal).let(::toSnafu)
}