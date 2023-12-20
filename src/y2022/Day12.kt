package y2022

import Day
import util.*
import java.util.*

object Day12 : Day(year = 2022) {

    private fun findAllDistances(start: Point): Map<Point, Int> {
        val distances = mutableMapOf<Point, Int>()
        val visited = mutableSetOf<Point>()
        val queue: Queue<PointWithCost> = LinkedList()
        queue.add(PointWithCost(start, 0))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.point in visited) continue
            visited.add(current.point)
            distances[current.point] = current.cost
            reverseEdges.filter { it.start == current.point && it.end !in visited }.forEach { edge ->
                queue.add(PointWithCost(edge.end, current.cost + 1))
            }
        }
        return distances
    }

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

    private val reverseEdges = lines.flatMapIndexed { row, line ->
        line.flatMapIndexed { col, c ->
            val current = row pTo col
            Direction.entries.mapNotNull {
                val point = current + it
                if (point.isInBounds(lines.lastIndex, lines.first().lastIndex) && c.height <= lines[point].height + 1) {
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
                row pTo it
            }
        }
        return bfs(start, end, edges).distance
    }

    override fun part2(): Any {
        val start = lines.withIndex().firstNotNullOf { (row, line) ->
            line.indexOf('E').takeUnless { it == -1 }?.let {
                row pTo it
            }
        }
        return findAllDistances(start).entries.filter {
            lines[it.key] == 'a' || lines[it.key] == 'S'
        }.minOf { it.value }
    }
}