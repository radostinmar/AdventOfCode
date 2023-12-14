package y2023

import Day
import util.accumulate
import util.transposedToChars
import java.util.Collections.swap

object Day14 : Day() {

    private fun List<List<Char>>.roll(): List<List<Char>> =
        this.map { line ->
            line.forEachIndexed { index, c ->
                if (c == 'O') {
                    val reverseIndex = line.subList(0, index).reversed().indexOfFirst { charAbove ->
                        charAbove == 'O' || charAbove == '#'
                    }.takeIf { it != -1 } ?: index
                    val foundIndex = index - reverseIndex
                    swap(line, index, foundIndex)
                }
            }
            line
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

    private fun detectCycle(numbers: List<Int>): Int {
        var size = 1
        val reversed = numbers.reversed()
        while (true) {
            if (size + size >= numbers.size) {
                return -1
            }
            if (reversed.subList(0, size) == reversed.subList(size, size + size)) {
                return size
            }
            size++
        }
    }

    override fun part1(): Any =
        lines.transposedToChars().roll().calculateWeight()

    override fun part2(): Any {
        var current = lines.transposedToChars()
        val testIterations = 300
        val results = accumulate(testIterations, listOf(current.calculateWeight())) { acc, _ ->
            current = current.cycle()
            acc + current.calculateWeight()
        }
        val cycleLength = detectCycle(results)
        return results[1000000000 % cycleLength + (testIterations / cycleLength - 1) * cycleLength]
    }
}