package y2022

import Day

object Day04 : Day(year = 2022) {

    private fun solve(condition: (firstStart: Int, firstEnd: Int, secondStart: Int, secondEnd: Int) -> Boolean): Int =
        lines.count { pair ->
            val (firstStart, firstEnd, secondStart, secondEnd) = pair.split('-', ',').map(String::toInt)
            condition(firstStart, firstEnd, secondStart, secondEnd)
        }

    override fun part1(): Any = solve { firstStart, firstEnd, secondStart, secondEnd ->
        firstStart >= secondStart && firstEnd <= secondEnd ||
                secondStart >= firstStart && secondEnd <= firstEnd
    }

    override fun part2(): Any = solve { firstStart, firstEnd, secondStart, secondEnd ->
        secondStart <= firstEnd && firstStart <= secondEnd
    }
}