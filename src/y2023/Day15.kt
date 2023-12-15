package y2023

import Day

object Day15 : Day() {

    private fun hash(string: String): Int =
        string.fold(0) { acc, cur ->
            ((acc + cur.code) * 17) % 256
        }

    override fun part1(): Any = lines.first().split(',').sumOf(::hash)

    override fun part2(): Any =
        lines.first().split(',').fold(mutableMapOf<Int, Map<String, Int>>()) { acc, entry ->
            when {
                '=' in entry -> {
                    val (label, focalLength) = entry.split('=')
                    acc.merge(hash(label), mapOf(label to focalLength.toInt()), Map<String, Int>::plus)
                }

                '-' in entry -> {
                    val label = entry.substringBefore('-')
                    acc.computeIfPresent(hash(label)) { _, value -> value.filterKeys { it != label } }
                }
            }
            acc
        }.toList().sumOf { (boxNumber, box) ->
            box.toList().withIndex().sumOf { (index, entry) ->
                (boxNumber + 1) * (index + 1) * entry.second
            }
        }
}