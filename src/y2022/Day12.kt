package y2022

import Day
import util.*

object Day12 : Day(year = 2022) {

    private val Char.height: Int
        get() = when (this) {
            'S' -> 0
            'E' -> 25
            else -> this.code - 'a'.code
        }

    private val edges = lines.flatMapIndexed { row, line ->
        line.flatMapIndexed { col, c ->
            val current = row pTo col
            Direction.entries.mapNotNull {
                val point = current + it
                if (point.isInBounds(lines.lastIndex, lines.first().lastIndex) && lines[point].height <= c.height + 1) {
                    Edge(current, point)
                } else {
                    null
                }
            }
        }
    }.toSet()

    override fun part1(): Any {
        val start = lines.withIndex().firstNotNullOf { (row, line) ->
            line.indexOf('S').takeUnless { it == -1 }?.let {
                row pTo it
            }
        }
        val end = lines.withIndex().firstNotNullOf { (row, line) ->
            line.indexOf('E').takeUnless { it == -1 }?.let {
                row pTo  it
            }
        }
        return aStar(start, end, edges).distance
    }

    override fun part2(): Any {
        val starts = lines.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, c ->
                if(c =='a' || c == 'S') row pTo col else null
            }
        }
        val end = lines.withIndex().firstNotNullOf { (row, line) ->
            line.indexOf('E').takeUnless { it == -1 }?.let {
                row pTo  it
            }
        }
        return starts.map { start -> aStar(start, end, edges).distance }
            .filter { it != -1 }
            .min()
    }
}