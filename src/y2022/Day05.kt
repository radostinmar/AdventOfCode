package y2022

import Day
import util.accumulate
import java.util.*

object Day05 : Day(year = 2022) {

    private fun solve(move: (stacks: MutableList<LinkedList<Char>>, amount: Int, from: Int, to: Int) -> Unit): String {
        val (cratesText, movesText) = text.split("\\R{2}".toRegex())
        val cratesLines = cratesText.split("\\R".toRegex())
        val stacks = "\\d".toRegex().findAll(cratesLines.last()).map { LinkedList<Char>() }.toMutableList()
        cratesLines.dropLast(1).forEach { row ->
            "[A-Z]".toRegex().findAll(row).forEach {
                stacks[(it.range.first + 3) / 4 - 1].addLast(it.value.first())
            }
        }
        movesText.split("\\R".toRegex()).forEach { moveString ->
            val (amount, from, to) = "\\d+".toRegex().findAll(moveString).map { it.value.toInt() }.toList()
            move(stacks, amount, from, to)
        }
        return stacks.joinToString("") { it.first().toString() }
    }

    override fun part1(): Any = solve { stacks, amount, from, to ->
        repeat(amount) {
            stacks[to - 1].addFirst(stacks[from - 1].pollFirst())
        }
    }

    override fun part2(): Any = solve { stacks, amount, from, to ->
        accumulate(amount, emptyList<Char>()) { acc, _ ->
            acc + stacks[from - 1].pollFirst()
        }.reversed().forEach { crate ->
            stacks[to - 1].addFirst(crate)
        }
    }
}