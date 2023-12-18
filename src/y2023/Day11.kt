package y2023

import Day
import util.Point
import util.manhattan
import util.pTo

object Day11 : Day() {

    private fun distance(
        first: Point,
        second: Point,
        emptyRows: List<Int>,
        emptyCols: List<Int>,
        expansion: Long
    ): Long {
        val expandedRows =
            emptyRows.count { it in first.x..second.x || it in second.x..first.x } * expansion
        val expandedCols =
            emptyCols.count { it in first.y..second.y || it in second.y..first.y } * expansion
        return manhattan(first, second) + expandedRows + expandedCols
    }

    private fun solve(expansion: Long): Number {
        val emptyCols = lines.first().indices.filter { index ->
            lines.all { it[index] == '.' }
        }

        val emptyRows = lines.mapIndexedNotNull { index, s ->
            if (s.all { it == '.' }) index else null
        }

        val galaxies = lines.flatMapIndexed { row, chars ->
            chars.mapIndexedNotNull { col, c ->
                if (c == '#') row pTo col else null
            }
        }
        return galaxies.withIndex().sumOf { galaxy ->
            galaxies.subList(galaxy.index + 1, galaxies.size).sumOf { nextGalaxy ->
                distance(galaxy.value, nextGalaxy, emptyRows, emptyCols, expansion)
            }
        }
    }

    override fun part1(): Number = solve(expansion = 1)

    override fun part2(): Number = solve(expansion = 999999)
}