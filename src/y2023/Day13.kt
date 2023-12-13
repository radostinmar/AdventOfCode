package y2023

import Day
import util.stringsTransposed
import kotlin.math.min

object Day13 : Day(isTest = false) {

    private fun List<String>.findMirror(
        matchCondition: (List<String>, List<String>) -> Boolean
    ): Int = this.indices.find { index ->
        val minSize = min(index, this.size - index)
        val left = this.subList(index - minSize, index)
        val right = this.subList(index, index + minSize).reversed()
        left.isNotEmpty() && matchCondition(left, right)
    } ?: 0

    private fun solve(matchCondition: (List<String>, List<String>) -> Boolean): Int =
        text.split("\\R{2}".toRegex()).map { it.split("\\R".toRegex()) }
            .sumOf { pattern ->
                pattern.findMirror(matchCondition) * 100 + pattern.stringsTransposed().findMirror(matchCondition)
            }

    override fun part1(): Number = solve { list1, list2 -> list1 == list2 }

    override fun part2(): Number = solve { list1, list2 ->
        list1.zip(list2).sumOf { (string1, string2) ->
            string1.zip(string2).count { (c1, c2) -> c1 != c2 }
        } == 1
    }
}