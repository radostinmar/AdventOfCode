fun main() {

    fun areAdjacent(sRow: Int, sCol: Int, numRow: Int, numCols: IntRange): Boolean =
        (sRow == numRow && (sCol == numCols.first - 1 || sCol == numCols.last + 1))
                || (sRow == numRow - 1 && sCol in (numCols.first - 1)..(numCols.last + 1))
                || (sRow == numRow + 1 && sCol in (numCols.first - 1)..(numCols.last + 1))

    fun part1(input: List<String>): Int {
        val symbols = input.flatMapIndexed { index: Int, s: String ->
            Regex("[^0-9.]").findAll(s).map { index to it.range.first }
        }.toSet()

        return input.flatMapIndexed { index: Int, s: String ->
            Regex("[0-9]+").findAll(s).map { index to it }
        }.sumOf { (numRow, matchResult) ->
            val hasAdjacent = symbols.any { (sRow, sCol) ->
                areAdjacent(sRow, sCol, numRow, matchResult.range)
            }
            if (hasAdjacent) matchResult.value.toInt() else 0
        }
    }

    fun part2(input: List<String>): Int {
        val numbers = input.flatMapIndexed { index: Int, s: String ->
            Regex("[0-9]+").findAll(s).map { index to it }
        }

        return input.flatMapIndexed { index: Int, s: String ->
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

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
