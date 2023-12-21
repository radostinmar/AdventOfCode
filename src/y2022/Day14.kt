package y2022

import Day
import util.Direction
import util.Point
import util.pTo
import kotlin.math.max
import kotlin.math.min

object Day14 : Day(year = 2022) {

    private fun getRocks(): MutableSet<Point> =
        lines.flatMap { line ->
            line.split(" -> ").map {
                val (y, x) = it.split(',')
                x.toInt() pTo y.toInt()
            }.zipWithNext { a, b ->
                (min(a.x, b.x)..max(a.x, b.x)).toList().flatMap { x ->
                    (min(a.y, b.y)..max(a.y, b.y)).toList().map { y ->
                        x pTo y
                    }
                }
            }.flatten()
        }.toMutableSet()

    private fun fall(start: Point, taken: Set<Point>, limit: Int?): Point? =
        if (start.x + 1 == limit) {
            start
        } else if (start + Direction.SOUTH !in taken) {
            if (limit == null && start.x > taken.maxOf { it.x }) {
                null
            } else {
                fall(start + Direction.SOUTH, taken, limit)
            }
        } else if (start + Direction.SOUTH + Direction.WEST !in taken) {
            fall(start + Direction.SOUTH + Direction.WEST, taken, limit)
        } else if (start + Direction.SOUTH + Direction.EAST !in taken) {
            fall(start + Direction.SOUTH + Direction.EAST, taken, limit)
        } else {
            start
        }

    override fun part1(): Any {
        val taken = getRocks()
        val start = 0 pTo 500
        var last = fall(start, taken, null)
        var fallenCount = 0
        while (last != null) {
            fallenCount++
            taken.add(last)
            last = fall(start, taken, null)
        }
        return fallenCount
    }

    override fun part2(): Any {
        val taken = getRocks()
        val start = 0 pTo 500
        val limit = taken.maxOf { it.x } + 2
        var fallenCount = 0
        while (true) {
            val current = fall(start, taken, limit) ?: return -1
            fallenCount++
            if (current == start) {
                return fallenCount
            }
            taken.add(current)
        }
    }
}