package y2022

import Day

object Day07 : Day(year = 2022) {

    private fun getSizes(): List<Int> {
        var currentDir = ""
        val sizes = mutableMapOf<String, Int>()
        val childrenMap = mutableMapOf<String, List<String>>()
        lines.forEach { line ->
            when {
                line == "\$ cd /" -> currentDir = "/"
                line == "\$ cd .." -> currentDir = currentDir.substringBeforeLast("/").ifEmpty { "/" }
                line == "\$ ls" -> Unit
                line.startsWith("\$ cd ") -> currentDir = "$currentDir/${line.substringAfter("\$ cd ")}"
                line.startsWith("dir") -> childrenMap.merge(
                    currentDir,
                    listOf("$currentDir/${line.substringAfter(" ")}"),
                    List<String>::plus
                )

                else -> sizes.merge(currentDir, line.substringBefore(" ").toInt(), Int::plus)
            }
        }
        childrenMap.entries.reversed().forEach { (parent, children) ->
            val childrenSize = children.sumOf(sizes::getValue)
            sizes.merge(parent, childrenSize, Int::plus)
        }
        return sizes.values.toList()
    }

    override fun part1(): Any = getSizes().filter { it < 100000 }.sum()

    override fun part2(): Any {
        val sizes = getSizes()
        val toDelete = sizes.first() - 40000000
        return sizes.filter { it > toDelete }.min()
    }
}