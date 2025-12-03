package y2025

import Day

object Day02 : Day(year = 2025) {

    override fun part1(): Any {
        var sum = 0L
        text.split(',').forEach { range ->
            val (start, end) = range.split('-').map { it.toLong() }
            for (id in start..end) {
                val idString = id.toString()
                val parts = idString.chunked((idString.length + 1) / 2)
                if (parts.size == 2 && parts[0] == parts[1]) {
                    sum += id
                }
            }
        }
        return sum
    }

    override fun part2(): Any {
        var sum = 0L
        text.split(',').forEach { range ->
            val (start, end) = range.split('-').map { it.toLong() }
            idLoop@ for (id in start..end) {
                val idString = id.toString()
                repeat(idString.length - 1) {
                    val chunkSize = it + 1
                    val parts = idString.chunked(chunkSize)
                    if (parts.all { part -> part == parts.first() }) {
                        sum += id
                        continue@idLoop
                    }
                }
            }
        }
        return sum
    }
}
