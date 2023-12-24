package y2023

import Day

object Day07 : Day(year = 2023) {
    private val normalSuitMap = mapOf(
        'A' to 12,
        'K' to 11,
        'Q' to 10,
        'J' to 9,
        'T' to 8,
        '9' to 7,
        '8' to 6,
        '7' to 5,
        '6' to 4,
        '5' to 3,
        '4' to 2,
        '3' to 1,
        '2' to 0
    )

    private val jokerSuitMap = mapOf(
        'A' to 12,
        'K' to 11,
        'Q' to 10,
        'T' to 9,
        '9' to 8,
        '8' to 7,
        '7' to 6,
        '6' to 5,
        '5' to 4,
        '4' to 3,
        '3' to 2,
        '2' to 1,
        'J' to 0,
    )

    private fun determineType(letters: MutableMap<Char, Int>): Type =
        when {
            letters.size == 1 -> Type.FiveOAK
            letters.size == 2 && letters.any { it.value == 4 } -> Type.FourOAK
            letters.size == 2 && letters.any { it.value == 3 } -> Type.FH
            letters.size == 3 && letters.any { it.value == 3 } -> Type.ThreeOAK
            letters.size == 3 && letters.any { it.value == 2 } -> Type.TP
            letters.size == 4 -> Type.OP
            else -> Type.HC
        }

    private fun getType(cards: String): Type {
        val letters = mutableMapOf<Char, Int>()
        cards.forEach {
            letters.merge(it, 1, Int::plus)
        }
        return determineType(letters)
    }

    private fun getTypeSecond(cards: String): Type {
        val letters = mutableMapOf<Char, Int>()
        cards.forEach {
            letters.merge(it, 1, Int::plus)
        }

        return if (letters.containsKey('J')) {
            if (letters.size == 1) {
                return Type.FiveOAK
            }
            val lettersNotJ = letters.filter { it.key != 'J' }
            val replaceWith = lettersNotJ.maxBy { it.value }.key
            getType(cards.replace('J', replaceWith))
        } else {
            determineType(letters)
        }
    }

    private enum class Type(val value: Int) {
        HC(0),
        OP(1),
        TP(2),
        ThreeOAK(3),
        FH(4),
        FourOAK(5),
        FiveOAK(6)
    }

    private data class Hand(val cards: String, val bid: Int, val withJokers: Boolean) : Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            val typeThis: Type
            val typeOther: Type
            if (withJokers) {
                typeThis = getTypeSecond(this.cards)
                typeOther = getTypeSecond(other.cards)
            } else {
                typeThis = getType(this.cards)
                typeOther = getType(other.cards)
            }
            val comparedByType = typeThis.value.compareTo(typeOther.value)
            if (comparedByType != 0) {
                return comparedByType
            }
            val suitMap = if (withJokers) jokerSuitMap else normalSuitMap
            this.cards.zip(other.cards).forEach { (tChar, oChar) ->
                val comparison = suitMap[tChar]!!.compareTo(suitMap[oChar]!!)
                if (comparison != 0) {
                    return comparison
                }
            }
            return 0
        }
    }

    private fun solve(withJokers: Boolean): Int =
        lines.map { line ->
            Hand(
                cards = line.substringBefore(" "),
                bid = line.substringAfter(" ").toInt(),
                withJokers = withJokers
            )
        }.sorted().mapIndexed { index, hand ->
            (index + 1) * hand.bid
        }.sum()

    override fun part1(): Number = solve(withJokers = false)

    override fun part2(): Number = solve(withJokers = true)
}
 