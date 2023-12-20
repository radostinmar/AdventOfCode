package y2022

import Day

object Day10 : Day(year = 2022) {

    override fun part1(): Any {
        var x = 1
        var cycle = 1
        val cycleValues = mutableMapOf<Int, Int>()
        lines.forEach { command ->
            cycleValues[cycle] = x
            if (command == "noop") {
                cycle++
            } else {
                cycle += 2
                x += command.substringAfter(" ").toInt()
            }
        }
        cycleValues[cycle] = x
        return List(6) { 20 + it * 40 }.sumOf { testCycle ->
            cycleValues.toList().last { it.first <= testCycle }.second * testCycle
        }
    }

    override fun part2(): Any {
        var x = 1
        var cycle = 1
        val cycleValues = mutableMapOf<Int, Int>()
        lines.forEach { command ->
            cycleValues[cycle] = x
            if (command == "noop") {
                cycle++
            } else {
                cycle += 2
                x += command.substringAfter(" ").toInt()
            }
        }
        val stringBuilder = StringBuilder()
        repeat(cycle - 1) { index ->
            if (index % 40 == 0) {
                stringBuilder.append("\n")
            }
            val spritePosition = cycleValues.toList().last { it.first <= index + 1 }.second
            if (index % 40 in (spritePosition - 1)..(spritePosition + 1)) {
                stringBuilder.append('⬜')
            } else {
                stringBuilder.append('⬛')
            }
        }
        return stringBuilder.toString()
    }
}