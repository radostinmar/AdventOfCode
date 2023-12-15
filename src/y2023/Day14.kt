package y2023

import Day
import util.swap
import util.transposedToChars

object Day14 : Day() {

    private fun List<List<Char>>.roll(): List<List<Char>> =
        this.map { line ->
            val updated = line.toMutableList()
            updated.forEachIndexed { index, c ->
                if (c == 'O') {
                    val reverseIndex = updated.subList(0, index)
                        .reversed()
                        .indexOfFirst { it != '.' }
                        .takeIf { it != -1 } ?: index
                    val foundIndex = index - reverseIndex
                    updated.swap(index, foundIndex)
                }
            }
            updated
        }

    private fun List<List<Char>>.calculateWeight(): Int =
        this.sumOf {
            it.withIndex().sumOf { value ->
                if (value.value == 'O') {
                    it.size - value.index
                } else {
                    0
                }
            }
        }

    private fun List<List<Char>>.rotate(): List<List<Char>> =
        this.indices.map { index ->
            this.map { it[this.lastIndex - index] }
        }

    private fun List<List<Char>>.cycle(): List<List<Char>> =
        this.roll().rotate()
            .roll().rotate()
            .roll().rotate()
            .roll().rotate()

    override fun part1(): Any =
        lines.transposedToChars().roll().calculateWeight()

    override fun part2(): Any {
        var current = lines.transposedToChars()
        val results = mutableListOf<List<List<Char>>>()

        while (true) {
            if (current in results) {
                break
            }
            results.add(current)
            current = current.cycle()
        }
        val cycleStart = results.lastIndexOf(current)
        val cycleSize = results.size - cycleStart
        return results[cycleStart + (1000000000 - cycleStart) % cycleSize].calculateWeight()
    }
}