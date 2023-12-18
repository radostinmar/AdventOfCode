package y2023

import Day
import util.Direction
import util.Point
import util.pTo
import kotlin.math.absoluteValue

object Day18 : Day(isTest = false) {

    private fun calculateArea(points: List<Pair<Point, Int>>): Long =
        points
            .zipWithNext { a, b -> (b.first.x + a.first.x).toLong() * (b.first.y - a.first.y) }
            .sum().absoluteValue / 2 + points.sumOf { it.second } / 2 + 1

    private fun solve(parse: (String) -> Pair<Direction, Int>): Long =
        lines.runningFold(0 pTo 0 to 0) { last, line ->
            val (direction, meters) = parse(line)
            last.first + direction.change * meters to meters
        }.let(::calculateArea)

    override fun part1(): Long = solve { line ->
        val (directionString, metersString) = line.split(" ")
        when (directionString) {
            "R" -> Direction.EAST
            "L" -> Direction.WEST
            "U" -> Direction.NORTH
            "D" -> Direction.SOUTH
            else -> throw IllegalStateException()
        } to metersString.toInt()
    }

    override fun part2(): Long =
        solve { line ->
            val instructions = line.substringAfterLast("#").dropLast(1)
            when (instructions.last()) {
                '0' -> Direction.EAST
                '2' -> Direction.WEST
                '3' -> Direction.NORTH
                '1' -> Direction.SOUTH
                else -> throw IllegalStateException()
            } to instructions.dropLast(1).toInt(radix = 16)
        }
}
