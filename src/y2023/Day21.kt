package y2023

import Day
import util.Direction
import util.Point
import util.get
import util.pTo

object Day21 : Day(year = 2023) {

    private fun getNeighbours(point: Point): List<Pair<Point, Point>> =
        Direction.entries.mapNotNull {
            val newPoint = point + it
            val xDiff = if (newPoint.x < 0) -1 else if (newPoint.x > lines.lastIndex) 1 else 0
            val yDiff = if (newPoint.y < 0) -1 else if (newPoint.y > lines.first().lastIndex) 1 else 0
            val normalizedPoint = newPoint + (-xDiff * lines.size pTo -yDiff * lines.first().length)
            if (lines[normalizedPoint] != '#') {
                normalizedPoint to (xDiff pTo yDiff)
            } else {
                null
            }
        }

    private val neighbours: MutableMap<Point, List<Pair<Point, Point>>> = lines.flatMapIndexed { row, s ->
        s.mapIndexedNotNull { col, _ ->
            val current = row pTo col
            current to getNeighbours(current)
        }
    }.toMap().toMutableMap()

    private fun findPossibleCount(steps: Int, saveAt: List<Int> = listOf(steps)): List<Int> {
        val saved = mutableListOf<Int>()
        val start = lines.withIndex().firstNotNullOf { (row, line) ->
            line.indexOf('S').takeUnless { it == -1 }?.let {
                row pTo it
            }
        }
        var currentPoints = setOf(start to (0 pTo 0))
        repeat(steps) { step ->
            currentPoints = currentPoints.flatMap { pair ->
                neighbours.getValue(pair.first)
                    .map {
                        it.first to (it.second + pair.second)
                    }
            }.toSet()
            if (step + 1 in saveAt) {
                saved.add(currentPoints.size)
            }
        }
        return saved
    }


    override fun part1(): Any = findPossibleCount(steps = 64).first()

    @Suppress("UnnecessaryVariable")
    override fun part2(): Any {
        val steps = 26501365
        val height = lines.size
        val remainder = steps % height

        val x0 = remainder
        val x1 = remainder + height
        val x2 = remainder + 2 * height

        val (y0, y1, y2) = findPossibleCount(x2, listOf(x0, x1, x2))
        val a = (y2 - 2 * y1 + y0) / 2L
        val b = y1 - y0 - a
        val c = y0
        val n = steps.toLong() / height

        return a * n * n + b * n + c
    }

}
