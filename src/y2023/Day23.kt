package y2023

import Day
import util.Direction
import util.Point
import util.get
import util.pTo

object Day23 : Day(isTest = false) {

    override fun part1(): Any {
        val start = 0 pTo lines.first().indexOfFirst { it == '.' }
        val end = lines.lastIndex pTo lines.last().indexOfFirst { it == '.' }
        return findMax(start, end, emptyList())
    }

    override fun part2(): Any {
        val start = 0 pTo lines.first().indexOfFirst { it == '.' }
        val end = lines.lastIndex pTo lines.last().indexOfFirst { it == '.' }
        val intersections = (findIntersections() + start + end).toSet()
        val graph = intersections.associateWith { findClosest(it, intersections) }
        return findMax2(start, end, emptySet(), graph)
    }

    private fun findMax2(
        start: Point,
        end: Point,
        visited: Set<Point>,
        graph: Map<Point, Map<Point, Int>>
    ): Int {
        if (start == end) {
            return 0
        }
        val neighbours = graph.getValue(start).filter { it.key !in visited }
        if(neighbours.isEmpty()) {
            return Int.MIN_VALUE
        }
        return neighbours.maxOf { entry ->
            entry.value + findMax2(entry.key, end, visited + start, graph)
        }
    }

    private fun findIntersections(): List<Point> = lines.flatMapIndexed { row, s ->
        s.mapIndexedNotNull { col, c ->
            if (c == '#') {
                null
            } else {
                val neighbours = getNeighbours(row pTo col).size
                if (neighbours > 2) {
                    row pTo col
                } else {
                    null
                }
            }
        }
    }

    private fun findClosest(start: Point, intersections: Set<Point>): Map<Point, Int> {
        val neighbours = getNeighbours(start)
        return neighbours.associate {
            findNextIntersection(it, setOf(start), intersections, 1)
        }
    }

    private fun findNextIntersection(
        start: Point,
        visited: Set<Point>,
        intersections: Set<Point>,
        distance: Int
    ): Pair<Point, Int> {
        val next = getNeighbours(start).first { it !in visited }
        return if (next in intersections) {
            next to (distance + 1)
        } else {
            findNextIntersection(next, visited + start, intersections, distance + 1)
        }
    }

    private fun getNeighbours(point: Point): List<Point> =
        Direction.entries.mapNotNull {
            val new = point + it
            if (new.isInBounds(lines.lastIndex, lines.first().lastIndex) && lines[new] != '#') {
                new
            } else {
                null
            }
        }

    private fun findMax(start: Point, end: Point, visited: List<Point>): Int {
        if (start == end) {
            return 0
        }
        val neighbours = when (lines[start]) {
            '.' -> Direction.entries.mapNotNull {
                val newPoint = start + it
                if (newPoint.x > 0) {
                    newPoint
                } else {
                    null
                }
            }

            '>' -> listOf(start + Direction.EAST)
            '<' -> listOf(start + Direction.WEST)
            '^' -> listOf(start + Direction.NORTH)
            'v' -> listOf(start + Direction.SOUTH)
            else -> emptyList()
        }.filter { lines[it] != '#' && it !in visited }
        if (neighbours.isEmpty()) {
            return Int.MIN_VALUE
        }
        return 1 + neighbours.maxOf {
            findMax(it, end, visited + start)
        }
    }
}
