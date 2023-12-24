package y2023

import Day
import kotlin.math.pow

object Day04 : Day(year = 2023) {
    private data class Card(
        val winning: Set<Int>,
        val numbers: List<Int>,
        val index: Int
    ) {
        val matches: Int
            get() = numbers.count { it in winning }
    }

    override fun part1(): Int = lines.sumOf { card ->
        val winning = card
            .replace(" +".toRegex(), " ")
            .substringAfter(": ")
            .substringBefore(" |")
            .split(" ")
            .map { it.toInt() }
            .toSet()

        val matching = card
            .replace(" +".toRegex(), " ")
            .substringAfter("| ")
            .split(" ")
            .map { it.toInt() }
            .count { it in winning }

        2f.pow(matching - 1).toInt()
    }

    override fun part2(): Int {
        val original = lines.mapIndexed { index, card ->
            val winning = card
                .replace(" +".toRegex(), " ")
                .substringAfter(": ")
                .substringBefore(" |")
                .split(" ")
                .map { it.toInt() }
                .toSet()

            val numbers = card
                .replace(" +".toRegex(), " ")
                .substringAfter("| ")
                .split(" ")
                .map { it.toInt() }

            Card(index = index, winning = winning, numbers = numbers)
        }

        val all = original.toMutableList()

        var i = 0
        while (i < all.size) {
            val card = all[i]
            i++
            val matches = card.matches
            if (matches > 0) {
                all.addAll(original.subList(card.index + 1, card.index + 1 + matches))
            }
        }

        return all.size
    }
}
