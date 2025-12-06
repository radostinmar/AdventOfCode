package y2025

import Day
import util.product
import util.productOf
import util.transposed
import util.zipWithPadding

object Day06 : Day(year = 2025, isTest = false) {

    override fun part1() =
        lines.map { line -> line.split("\\s+".toRegex()).filter { it.isNotBlank() } }
            .transposed()
            .sumOf { problem ->
                val operation = problem.last()
                if (operation == "+") {
                    problem.dropLast(1).sumOf { it.toLong() }
                } else {
                    problem.dropLast(1).productOf { it.toLong() }
                }
            }

    override fun part2(): Any {
        var sum = 0L
        val numbers = mutableListOf<Long>()
        zipWithPadding(lines.map { it.toList() }, padding = ' ')
            .reversed()
            .forEach { row ->
                val newNumber = row
                    .dropLast(1)
                    .joinToString("")
                    .trim()
                    .ifEmpty { return@forEach }
                    .toLong()

                numbers.add(newNumber)

                val operation = row.last()
                when (operation) {
                    '+' -> {
                        sum += numbers.sum()
                        numbers.clear()
                    }

                    '*' -> {
                        sum += numbers.product()
                        numbers.clear()
                    }
                }
            }
        return sum
    }
}
