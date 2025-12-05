package y2025

import Day
import util.split

object Day05 : Day(year = 2025) {

    override fun part1(): Any {
        val (rawRanges, ingredients) = lines.split("")
        val ranges = rawRanges.map { range ->
            val (start, end) = range.split('-')
            start.toLong()..end.toLong()
        }
        return ingredients.map { it.toLong() }.count { ingredient ->
            ranges.any { range -> ingredient in range }
        }
    }

    override fun part2(): Long {
        val ranges = lines
            .split("")
            .first()
            .map { range ->
                val (start, end) = range.split('-')
                start.toLong()..end.toLong()
            }
            .sortedBy { it.first }
        val mergedRanges = mutableListOf<LongRange>()
        ranges.forEach { range ->
            println(range)
            val intersectingWith = mergedRanges.indexOfFirst { mergedRange ->
                range.first in mergedRange || range.last in mergedRange
            }
            if (intersectingWith != -1) {
                val oldMergedRange = mergedRanges[intersectingWith]
                val newStart = if (range.first in oldMergedRange) oldMergedRange.first else range.first
                val newEnd = if (range.last in oldMergedRange) oldMergedRange.last else range.last
                val newRange = newStart..newEnd
                if (newRange != oldMergedRange) {
                    mergedRanges.remove(oldMergedRange)
                    mergedRanges.add(newRange)
                }
            } else {
                mergedRanges.add(range)
            }
        }
        return mergedRanges.sumOf { it.last - it.first + 1 }
    }
}
