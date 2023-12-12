package y2022

import Day
import util.toInts

object Day01 : Day(year = 2022) {
    override fun part1(): Number =
        text.split("\\R{2}".toRegex()).maxOf {
            it.split("\\R".toRegex()).toInts().sum()
        }

    override fun part2(): Number =
        text.split("\\R{2}".toRegex()).map {
            it.split("\\R".toRegex()).toInts().sum()
        }.sortedDescending().take(3).sum()
}