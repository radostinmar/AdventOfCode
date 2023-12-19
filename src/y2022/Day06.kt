package y2022

import Day

object Day06 : Day(year = 2022) {

    private fun solve(size: Int): Int =
        lines.first().windowedSequence(size).indexOfFirst { window ->
            window.toSet().size == window.length
        } + size

    override fun part1(): Any = solve(4)

    override fun part2(): Any = solve(14)
}