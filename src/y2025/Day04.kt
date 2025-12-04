package y2025

import Day

object Day04 : Day(year = 2025) {

    override fun part1(): Any {
        var count = 0
        lines.forEachIndexed { xIndex, line ->
            line.forEachIndexed { yIndex, char ->
                if (char == '.') return@forEachIndexed
                val emptyAdjacent = listOf(
                    xIndex - 1 to yIndex - 1,
                    xIndex - 1 to yIndex,
                    xIndex - 1 to yIndex + 1,
                    xIndex to yIndex - 1,
                    xIndex to yIndex + 1,
                    xIndex + 1 to yIndex - 1,
                    xIndex + 1 to yIndex,
                    xIndex + 1 to yIndex + 1
                ).count { (adjacentX, adjacentY) ->
                    lines.getOrNull(adjacentX)?.getOrNull(adjacentY) != '@'
                }
                if (emptyAdjacent > 4) count++
            }
        }
        return count
    }

    override fun part2(): Any {
        var count = 0
        var currentState = lines.map { it.toList() }
        var newState: List<List<Char>> = currentState
        do {
            currentState = newState
            newState = currentState.mapIndexed { xIndex, line ->
                line.mapIndexed { yIndex, char ->
                    if (char == '.') return@mapIndexed '.'
                    val emptyAdjacent = listOf(
                        xIndex - 1 to yIndex - 1,
                        xIndex - 1 to yIndex,
                        xIndex - 1 to yIndex + 1,
                        xIndex to yIndex - 1,
                        xIndex to yIndex + 1,
                        xIndex + 1 to yIndex - 1,
                        xIndex + 1 to yIndex,
                        xIndex + 1 to yIndex + 1
                    ).count { (adjacentX, adjacentY) ->
                        currentState.getOrNull(adjacentX)?.getOrNull(adjacentY) != '@'
                    }
                    return@mapIndexed if (emptyAdjacent > 4) {
                        count++
                        '.'
                    } else {
                        '@'
                    }
                }
            }

        } while (newState != currentState)
        return count
    }
}
