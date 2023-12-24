package y2023

import Day
import util.split
import util.toLongs

object Day05 : Day(year = 2023) {
    private data class Mapping(val src: Long, val dst: Long, val size: Long) {
        fun mapValue(value: Long): Long? = if (value in src..(src + size)) {
            value + dst - src
        } else {
            null
        }
    }

    private fun List<Mapping>.mapValue(value: Long): Long = this.firstNotNullOfOrNull { it.mapValue(value) } ?: value

    private fun getMappings(grouped: List<List<String>>, reversed: Boolean = false): List<List<Mapping>> =
        grouped.drop(1).map {
            it.drop(1).map { mapping ->
                val split = mapping.split(" ").toLongs()
                Mapping(
                    src = if (reversed) split[0] else split[1],
                    dst = if (reversed) split[1] else split[0],
                    size = split[2]
                )
            }
        }.let { if (reversed) it.reversed() else it }

    override fun part1(): Long {
        val grouped = lines.split("")
        val seeds = grouped.first().first().split(" ").drop(1).toLongs()
        val mappings = getMappings(grouped)
        return seeds.minOf {
            var currentValue = it
            mappings.forEach { mapping ->
                currentValue = mapping.mapValue(currentValue)
            }
            currentValue
        }
    }

    override fun part2(): Long {
        val grouped = lines.split("")
        val seeds = grouped.first().first().split(" ").drop(1).toLongs().chunked(2)
        val mappings = getMappings(grouped, reversed = true)
        var testVal = 0L
        while (true) {
            var current = testVal
            mappings.forEach { mapping ->
                current = mapping.mapValue(current)
            }
            if (seeds.any { current in it[0].until(it[0] + it[1]) }) {
                return testVal
            }
            testVal++
        }
    }
}
