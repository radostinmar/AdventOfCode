package y2022

import Day
import util.product

object Day13 : Day(year = 2022) {

    private fun String.splitToList(): List<String> {
        var currentString = ""
        val result = mutableListOf<String>()
        var opened = 0

        this.drop(1).dropLast(1).forEach {
            when (it) {
                '[' -> {
                    currentString += it
                    opened++
                }

                ']' -> {
                    currentString += it
                    opened--
                }

                ',' -> if (opened == 0) {
                    result.add(currentString)
                    currentString = ""
                } else {
                    currentString += it
                }

                else -> currentString += it
            }
        }

        if (currentString.isNotEmpty()) {
            result.add(currentString)
        }

        return result
    }

    private fun compare(first: String, second: String): Int =
        if (first.startsWith('[')) {
            if (second.startsWith('[')) {
                val firstList = first.splitToList()
                val secondList = second.splitToList()
                compareLists(firstList, secondList)
            } else {
                val firstList = first.splitToList()
                val secondList = listOf(second)
                compareLists(firstList, secondList)
            }
        } else {
            if (second.startsWith('[')) {
                val firstList = listOf(first)
                val secondList = second.splitToList()
                compareLists(firstList, secondList)
            } else {
                first.toInt().compareTo(second.toInt())
            }
        }

    private fun compareLists(first: List<String>, second: List<String>): Int {
        val comparisons = first.zip(second).map { compare(it.first, it.second) }
        val firstNotEqual = comparisons.find { it != 0 }
        return firstNotEqual ?: first.size.compareTo(second.size)
    }

    override fun part1(): Any =
        text.split("\\R{2}".toRegex()).map { pair ->
            pair.split("\\R".toRegex()).let { it[0] to it[1] }
        }.mapIndexedNotNull { index, pair ->
            if (compare(pair.first, pair.second) == -1) index + 1 else null
        }.sum()

    override fun part2(): Any {
        val dividers = listOf("[[2]]", "[[6]]")
        val sorted = text.split("\\R{2}".toRegex()).flatMap { pair ->
            pair.split("\\R".toRegex())
        }.plus(dividers)
            .sortedWith(::compare)
        return dividers.map { sorted.indexOf(it) + 1 }.product
    }
}