package y2023

import Day

object Day02 : Day() {

    override fun part1(): Int = lines.sumOf { game ->
        val valid = game.substringAfter(": ").split("; ").none { subset ->
            subset.split(", ").any { cubes ->
                val color = cubes.substringAfter(" ")
                val amount = cubes.substringBefore(" ").toInt()
                when (color) {
                    "red" -> amount > 12
                    "green" -> amount > 13
                    "blue" -> amount > 14
                    else -> throw IllegalStateException()
                }
            }
        }
        if (valid) {
            game.substringBefore(':').substringAfter("Game ").toInt()
        } else {
            0
        }
    }


    override fun part2(): Int = lines.sumOf { game ->
        var redMax = Int.MIN_VALUE
        var greenMax = Int.MIN_VALUE
        var blueMax = Int.MIN_VALUE
        game.substringAfter(": ").split("; ").forEach { subset ->
            subset.split(", ").forEach { cubes ->
                val color = cubes.substringAfter(" ")
                val amount = cubes.substringBefore(" ").toInt()
                when (color) {
                    "red" -> if (amount > redMax) redMax = amount
                    "green" -> if (amount > greenMax) greenMax = amount
                    "blue" -> if (amount > blueMax) blueMax = amount
                }
            }
        }
        redMax * blueMax * greenMax
    }
}
