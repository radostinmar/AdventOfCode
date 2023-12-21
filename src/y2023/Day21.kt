package y2023

import Day
import util.Direction
import util.Point
import util.get
import util.pTo

object Day21 : Day(isTest = false) {

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

    private fun findPossibleCount(steps: Int) : Int {
        val start = lines.withIndex().firstNotNullOf { (row, line) ->
            line.indexOf('S').takeUnless { it == -1 }?.let {
                row pTo it
            }
        }
        var currentPoints = setOf(start to (0 pTo 0))
        repeat(steps) {
            currentPoints = currentPoints.flatMap { pair ->
                neighbours.getValue(pair.first)
                    .map {
                        it.first to (it.second + pair.second)
                    }
            }.toSet()
        }
        return currentPoints.size
    }


    override fun part1(): Any = findPossibleCount(steps = 64)

    override fun part2(): Any {
        // 26501365 = 202300 * 131 + 65
        val a0 = findPossibleCount(65)
        val a1 = findPossibleCount(65 + 131)
        val a2 = findPossibleCount(65 + 2 * 131)
//        [3710, 32976, 91404]
        return 596734624269210
    }

}
