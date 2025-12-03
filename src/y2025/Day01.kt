package y2025

import Day

object Day01 : Day(year = 2025) {

    override fun part1(): Any {
        var dial = 50
        var counter = 0
        lines.forEach { line ->
            val direction = line.first()
            val clicks = line.drop(1).toInt()
            val rotation = if (direction == 'R') clicks else -clicks
            dial = (dial + rotation) % 100
            if (dial == 0) counter++
        }
        return counter
    }

    override fun part2(): Any {
        var dial = 50
        var counter = 0
        lines.forEach { line ->
            val direction = line.first()
            val clicks = line.drop(1).toInt()
            repeat(clicks) {
                val rotation = if (direction == 'R') 1 else -1
                dial = (dial + rotation) % 100
                if (dial == 0) counter++
            }

        }
        return counter
    }
}
