package y2023

import Day

object Day15 : Day(year = 2023) {

    private fun hash(string: String): Int =
        string.fold(0) { acc, cur ->
            ((acc + cur.code) * 17) % 256
        }

    override fun part1(): Any = lines.first().split(',').sumOf(::hash)

    override fun part2(): Any =
        lines.first().split(',').fold(MutableList(256) { (mapOf<String, Int>()) }) { acc, entry ->
            when {
                '=' in entry -> {
                    val (label, focalLength) = entry.split('=')
                    acc[hash(label)] += mapOf(label to focalLength.toInt())
                }

                '-' in entry -> {
                    val label = entry.substringBefore('-')
                    acc[hash(label)] = acc[hash(label)] - label
                }
            }
            acc
        }.withIndex().sumOf { (boxNumber, box) ->
            box.toList().withIndex().sumOf { (index, entry) ->
                (boxNumber + 1) * (index + 1) * entry.second
            }
        }
}