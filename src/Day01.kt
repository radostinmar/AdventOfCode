fun main() {
    val wordsToDigits = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    fun part1(input: List<String>): Int =
        input.sumOf { line ->
            (line.first { it.isDigit() }.toString() + line.last { it.isDigit() }).toInt()
        }

    fun part2(input: List<String>): Int =
        input.sumOf { line ->
            val first = line.findAnyOf(wordsToDigits.keys + wordsToDigits.values)!!.second.let { number ->
                (wordsToDigits[number] ?: number)
            }

            val last = line.findLastAnyOf(wordsToDigits.keys + wordsToDigits.values)!!.second.let { number ->
                (wordsToDigits[number] ?: number)
            }

            (first + last).toInt()
        }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
