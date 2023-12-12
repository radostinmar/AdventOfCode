package y2023

import Day
import util.productOf
import util.quadratic
import kotlin.math.ceil
import kotlin.math.floor

object Day06 : Day() {

    private fun findPossible(time: Long, distance: Long): Long {
        val (x1, x2) = quadratic(1.0, -time.toDouble(), distance.toDouble())
        return floor(x1 - 0.00000001).toLong() - ceil(x2 + 0.00000001).toLong() + 1
    }

    override fun part1(): Number {
        val times = "\\d+".toRegex().findAll(lines[0]).map { it.value.toLong() }.toList()
        val records = "\\d+".toRegex().findAll(lines[1]).map { it.value.toLong() }.toList()
        return times.zip(records).productOf { (time, record) ->
            findPossible(time, record).toInt()
        }
    }

    override fun part2(): Number {
        val time = "\\d+".toRegex().findAll(lines[0])
            .joinToString(separator = "", transform = MatchResult::value).toLong()
        val record = "\\d+".toRegex().findAll(lines[1])
            .joinToString(separator = "", transform = MatchResult::value).toLong()
        return findPossible(time, record)
    }
}
