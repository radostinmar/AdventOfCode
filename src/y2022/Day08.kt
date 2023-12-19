package y2022

import Day

object Day08 : Day(year = 2022) {

    private fun isVisible(rowIndex: Int, colIndex: Int, height: Int): Boolean {
        val col = lines.map { it[colIndex].digitToInt() }
        val row = lines[rowIndex].map(Char::digitToInt)
        val above = col.subList(0, rowIndex)
        val below = col.subList(rowIndex + 1, col.size)
        val left = row.toList().subList(0, colIndex)
        val right = row.subList(colIndex + 1, row.size)
        return above.all { it < height }
                || below.all { it < height }
                || left.all { it < height }
                || right.all { it < height }
    }

    private fun findVisible(height: Int, trees: List<Int>): Int =
        trees.indexOfFirst { it >= height }.takeUnless { it == -1 }
            ?.let { it + 1 }
            ?: trees.size

    private fun getScenicScore(rowIndex: Int, colIndex: Int, height: Int): Int {
        val col = lines.map { it[colIndex].digitToInt() }
        val row = lines[rowIndex].map(Char::digitToInt)
        val above = col.subList(0, rowIndex)
        val below = col.subList(rowIndex + 1, col.size)
        val left = row.toList().subList(0, colIndex)
        val right = row.subList(colIndex + 1, row.size)
        return findVisible(height, above.reversed()) *
                findVisible(height, below) *
                findVisible(height, left.reversed()) *
                findVisible(height, right)
    }

    override fun part1(): Any = lines.withIndex().sumOf { (row, line) ->
        line.withIndex().count { (col, tree) ->
            isVisible(row, col, tree.digitToInt())
        }
    }

    override fun part2(): Any = lines.withIndex().flatMap { (row, line) ->
        line.withIndex().map { (col, tree) ->
            getScenicScore(row, col, tree.digitToInt())
        }
    }.max()
}