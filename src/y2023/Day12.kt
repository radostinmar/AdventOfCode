package y2023

import Day
import util.toInts

object Day12 : Day(year = 2023) {

    private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    private fun String.substringOrEmpty(startIndex: Int): String =
        if (startIndex > this.length) "" else this.substring(startIndex)

    private fun findCombinations(symbols: String, numbers: List<Int>): Long {
        cache[symbols to numbers]?.let { return it }
        if (numbers.isEmpty()) {
            return if ('#' in symbols) 0 else 1
        }
        val size = numbers.first()
        var total = 0L
        run {
            symbols.forEachIndexed { index, c ->
                if (
                    index + size <= symbols.length
                    && '.' !in symbols.substring(index, index + size)
                    && (index == 0 || symbols[index - 1] != '#')
                    && (index + size == symbols.length || symbols[index + size] != '#')
                ) {
                    total += findCombinations(symbols.substringOrEmpty(index + size + 1), numbers.drop(1))
                }

                if (c == '#') return@run
            }
        }
        cache[symbols to numbers] = total
        return total
    }

    private fun unfold(input: String, copies: Int, separator: String): String =
        List(copies) { input }.joinToString(separator)

    private fun solve(copies: Int): Long = lines.sumOf { record ->
        val (symbols, numbers) = record.split(" ")
        findCombinations(
            unfold(symbols, copies, "?"),
            unfold(numbers, copies, ",").split(',').toInts()
        )
    }

    override fun part1(): Number = solve(copies = 1)

    override fun part2(): Number = solve(copies = 5)
}