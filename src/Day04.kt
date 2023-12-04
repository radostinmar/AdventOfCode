import kotlin.math.pow

data class Card(
    val winning: Set<Int>,
    val numbers: List<Int>,
    val index: Int
) {
    val matches: Int
        get() = numbers.count { it in winning }
}

fun main() {

    fun part1(input: List<String>): Int = input.sumOf { card ->
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

    fun part2(input: List<String>): Int {
        val original = input.mapIndexed { index, card ->
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

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
