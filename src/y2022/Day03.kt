package y2022

import Day

object Day03 : Day(year = 2022) {

    private fun getPriority(char: Char): Int =
        if (char.isLowerCase()) char.code - 'a'.code + 1 else char.code - 'A'.code + 27

    override fun part1(): Any = lines.sumOf { rucksack ->
        val first = rucksack.substring(0, rucksack.length / 2).toSet()
        val second = rucksack.substring(rucksack.length / 2, rucksack.length).toSet()
        val common = first.intersect(second).first()
        getPriority(common)
    }

    override fun part2(): Any = lines.chunked(3).sumOf { group ->
        val common = group.map(String::toSet).reduce { acc, chars -> acc.intersect(chars) }.first()
        getPriority(common)
    }
}