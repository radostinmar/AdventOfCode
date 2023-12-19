package y2022

import Day

object Day06 : Day(year = 2022) {

    private fun solve(size: Int): Int {
        var lastFour = emptyList<Char>()
        lines.first().forEachIndexed { index, c ->
            lastFour = (lastFour + c).takeLast(size)
            if (lastFour.distinct().size == size) {
                return index + 1
            }
        }
        return -1
    }

    override fun part1(): Any = solve(4)

    override fun part2(): Any = solve(14)
}