package y2025

import Day

object Day03 : Day(year = 2025) {

    override fun part1(): Any = lines.sumOf { line ->
        val max = line.dropLast(1).maxBy { it.digitToInt() }
        val nextMax = line
            .substring(startIndex = line.indexOf(max) + 1)
            .maxOf { it.digitToInt() }
        "$max$nextMax".toInt()
    }

    override fun part2(): Any = lines.sumOf { line ->
        var startIndex = 0
        (0..<12).map {
            val max = line
                .substring(startIndex, line.length - 11 + it)
                .maxBy { digit -> digit.digitToInt() }
            startIndex = line.indexOf(max, startIndex = startIndex) + 1
            max
        }.joinToString(separator = "").toLong()
    }
}
