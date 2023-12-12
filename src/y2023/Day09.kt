package y2023

import Day
import util.toInts

object Day09 : Day() {
    private fun generateNext(numbers: List<Int>): List<Int>? =
        if (numbers.all { it == 0 }) {
            null
        } else {
            numbers.mapIndexedNotNull { index, number ->
                numbers.getOrNull(index - 1)?.let { previous ->
                    number - previous
                }
            }
        }

    private fun solve(isFirst: Boolean): Int = lines.sumOf { line ->
        generateSequence(line.split(" ").toInts(), Day09::generateNext)
            .toList()
            .reversed()
            .fold(0) { acc, current ->
                if (isFirst) {
                    acc + current.last()
                } else {
                    current.first() - acc
                }
            }.toInt()
    }

    override fun part1(): Number = solve(isFirst = true)

    override fun part2(): Number = solve(isFirst = false)
}