package y2023

import Day

object Day15 : Day(isTest = false) {

    private fun hash(string: String): Int =
        string.fold(0) { acc, cur ->
            ((acc + cur.code) * 17) % 256
        }

    override fun part1(): Any = lines.first().split(',').sumOf(::hash)

    override fun part2(): Any {
        val boxes = mutableMapOf<Int, Map<String, Int>>()
        lines.first().split(',').forEach { entry ->
            when {
                '=' in entry -> {
                    val (label, focalLength) = entry.split('=')
                    val hash = hash(label)
                    boxes.merge(hash, mapOf(label to focalLength.toInt()), Map<String, Int>::plus)
                }

                '-' in entry -> {
                    val label = entry.substringBefore('-')
                    val hash = hash(label)
                    boxes[hash]?.filterKeys { it != label }?.let { boxes[hash] = it }
                }
            }
        }
        return boxes.toList().sumOf { (boxNumber, box) ->
            box.toList().withIndex().sumOf { (index, entry) ->
                (boxNumber + 1) * (index + 1) * entry.second
            }
        }
    }
}