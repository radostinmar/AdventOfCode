package y2023

import Day

object Day01 : Day(year = 2023) {
    private val wordsToDigits = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    override fun part1(): Int =
        lines.sumOf { line ->
            (line.first { it.isDigit() }.toString() + line.last { it.isDigit() }).toInt()
        }

    override fun part2(): Int =
        lines.sumOf { line ->
            val first = line.findAnyOf(wordsToDigits.keys + wordsToDigits.values)!!.second.let { number ->
                (wordsToDigits[number] ?: number)
            }

            val last = line.findLastAnyOf(wordsToDigits.keys + wordsToDigits.values)!!.second.let { number ->
                (wordsToDigits[number] ?: number)
            }

            (first + last).toInt()
        }
}
