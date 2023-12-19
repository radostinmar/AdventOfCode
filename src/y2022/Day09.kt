package y2022

import Day
import util.Direction
import util.Point
import util.pTo
import kotlin.math.absoluteValue
import kotlin.math.sign

object Day09 : Day(year = 2022) {

    private fun follow(current: Point, previous: Point): Point {
        val difference = previous - current
        return if (difference.x.absoluteValue > 1 || difference.y.absoluteValue > 1) {
            current + (difference.x.sign pTo difference.y.sign)
        } else {
            current
        }
    }

    private fun move(head: Point, directionString: String): Point =
        head + when (directionString) {
            "R" -> Direction.EAST
            "L" -> Direction.WEST
            "U" -> Direction.NORTH
            "D" -> Direction.SOUTH
            else -> throw IllegalStateException()
        }

    override fun part1(): Any {
        var head = 0 pTo 0
        var tail = 0 pTo 0
        val visited = mutableSetOf(tail)
        lines.forEach { move ->
            val (direction, times) = move.split(" ")
            repeat(times.toInt()) {
                head = move(head, direction)
                tail = follow(tail, head)
                visited.add(tail)
            }
        }
        return visited.size
    }

    override fun part2(): Any {
        var head = 0 pTo 0
        var knots = List(9) { 0 pTo 0 }
        val visited = mutableSetOf(knots.last())
        lines.forEach { move ->
            val (direction, times) = move.split(" ")
            repeat(times.toInt()) {
                head = move(head, direction)
                knots = knots
                    .runningFold(head) { previous, knot -> follow(knot, previous) }
                    .drop(1)
                visited.add(knots.last())
            }
        }
        return visited.size
    }
}