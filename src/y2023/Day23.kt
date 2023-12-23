package y2023

import Day
import util.Direction
import util.Point
import util.get
import util.pTo

object Day23 : Day(isTest = false) {

    override fun part1(): Any = solve(withSlopes = true)

    override fun part2(): Any = solve(withSlopes = false)

    private fun solve(withSlopes: Boolean): Int {
        val start = 0 pTo lines.first().indexOfFirst { it == '.' }
        val end = lines.lastIndex pTo lines.last().indexOfFirst { it == '.' }
        val intersections = (findIntersections(withSlopes) + start + end).toSet()
        val graph = intersections.associateWith { findClosest(it, intersections, withSlopes) }
        return findMax(start, end, emptySet(), graph)
    }

    private fun findMax(
        start: Point,
        end: Point,
        visited: Set<Point>,
        graph: Map<Point, Map<Point, Int>>
    ): Int {
        if (start == end) {
            return 0
        }
        val neighbours = graph.getValue(start).filter { it.key !in visited }
        if (neighbours.isEmpty()) {
            return Int.MIN_VALUE
        }
        return neighbours.maxOf { entry ->
            entry.value + findMax(entry.key, end, visited + start, graph)
        }
    }

    private fun findIntersections(withSlopes: Boolean): List<Point> = lines.flatMapIndexed { row, s ->
        s.mapIndexedNotNull { col, c ->
            if (c == '#') {
                null
            } else {
                val neighbours = getNeighbours(row pTo col, withSlopes).size
                if (neighbours > 2) {
                    row pTo col
                } else {
                    null
                }
            }
        }
    }

    private fun findClosest(start: Point, intersections: Set<Point>, withSlopes: Boolean): Map<Point, Int> {
        val neighbours = getNeighbours(start, withSlopes)
        return neighbours.mapNotNull {
            findNextIntersection(it, setOf(start), intersections, 1, withSlopes)
        }.toMap()
    }

    private fun findNextIntersection(
        start: Point,
        visited: Set<Point>,
        intersections: Set<Point>,
        distance: Int,
        withSlopes: Boolean
    ): Pair<Point, Int>? {
        val next = getNeighbours(start, withSlopes).find { it !in visited }
        next ?: return null
        return if (next in intersections) {
            next to (distance + 1)
        } else {
            findNextIntersection(next, visited + start, intersections, distance + 1, withSlopes)
        }
    }

    private fun getNeighbours(point: Point, withSlopes: Boolean): List<Point> =
        Direction.entries.mapNotNull {
            if (withSlopes) {
                when (lines[point]) {
                    '>' -> if (it != Direction.EAST) return@mapNotNull null
                    '<' -> if (it != Direction.WEST) return@mapNotNull null
                    '^' -> if (it != Direction.NORTH) return@mapNotNull null
                    'v' -> if (it != Direction.SOUTH) return@mapNotNull null
                }
            }
            val new = point + it
            if (new.isInBounds(lines.lastIndex, lines.first().lastIndex) && lines[new] != '#') {
                new
            } else {
                null
            }
        }
}
