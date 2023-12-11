import kotlin.math.abs

object Day11 : Day(isTest = false) {

    private fun distance(
        first: Pair<Int, Int>,
        second: Pair<Int, Int>,
        emptyRows: List<Int>,
        emptyCols: List<Int>,
        expansion: Long
    ): Long {
        val expandedRows =
            emptyRows.count { it in first.first..second.first || it in second.first..first.first } * expansion
        val expandedCols =
            emptyCols.count { it in first.second..second.second || it in second.second..first.second } * expansion
        return abs(first.first - second.first) + abs(first.second - second.second) + expandedRows + expandedCols
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
                if (c == '#') row to col else null
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