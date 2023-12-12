package y2023

import Day
import util.product

object Day03 : Day() {

    private fun areAdjacent(sRow: Int, sCol: Int, numRow: Int, numCols: IntRange): Boolean =
        (sRow == numRow && (sCol == numCols.first - 1 || sCol == numCols.last + 1))
                || (sRow == numRow - 1 && sCol in (numCols.first - 1)..(numCols.last + 1))
                || (sRow == numRow + 1 && sCol in (numCols.first - 1)..(numCols.last + 1))

    override fun part1(): Int {
        val symbols = lines.flatMapIndexed { index: Int, s: String ->
            Regex("[^0-9.]").findAll(s).map { index to it.range.first }
        }.toSet()

        return lines.flatMapIndexed { index: Int, s: String ->
            Regex("[0-9]+").findAll(s).map { index to it }
        }.sumOf { (numRow, matchResult) ->
            val hasAdjacent = symbols.any { (sRow, sCol) ->
                areAdjacent(sRow, sCol, numRow, matchResult.range)
            }
            if (hasAdjacent) matchResult.value.toInt() else 0
        }
    }

    override fun part2(): Int {
        val numbers = lines.flatMapIndexed { index: Int, s: String ->
            Regex("[0-9]+").findAll(s).map { index to it }
        }

        return lines.flatMapIndexed { index: Int, s: String ->
            Regex("\\*").findAll(s).map { index to it.range.first }
        }.sumOf { (sRow, sCol) ->
            val adjacent = numbers.filter { (numRow, matchResult) ->
                areAdjacent(sRow, sCol, numRow, matchResult.range)
            }
            if (adjacent.size == 2) {
                adjacent.map { it.second.value.toInt() }.product
            } else {
                0
            }
        }
    }
}
