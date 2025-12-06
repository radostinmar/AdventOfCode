package y2022

import Day
import util.product
import util.toLongs
import java.util.*

object Day11 : Day(year = 2022) {

    private class Monkey(
        val items: Queue<Long>,
        val operation: (Long) -> Long,
        val testNumber: Long,
        val trueMonkey: Int,
        val falseMonkey: Int
    ) {
        var inspectedItems = 0L

        fun performTurn(monkeys: List<Monkey>, decrease: (Long) -> Long) {
            inspectedItems += items.size
            while (items.isNotEmpty()) {
                val item = items.poll()
                val inspected = decrease(operation(item))
                val target = if (inspected % testNumber == 0L) trueMonkey else falseMonkey
                monkeys[target].items.add(inspected)
            }
        }
    }

    private fun getMonkeys(): List<Monkey> =
        text.split("\\R{2}".toRegex()).map { monkeyText ->
            val monkeyLines = monkeyText.split("\\R".toRegex()).drop(1)
            val items = monkeyLines[0].substringAfter(": ").split(", ").toLongs()
            val (_, operationSymbol, second) = monkeyLines[1].substringAfter("= ").split(" ")
            val testNumber = monkeyLines[2].substringAfter("by ").toLong()
            val trueMonkey = monkeyLines[3].substringAfter("monkey ").toInt()
            val falseMonkey = monkeyLines[4].substringAfter("monkey ").toInt()
            val secondNumber = second.takeUnless { it == "old" }?.toLong()
            val operation: (Long) -> Long =
                if (operationSymbol == "*") {
                    { number: Long -> number * (secondNumber ?: number) }
                } else {
                    { number: Long -> number + (secondNumber ?: number) }
                }
            Monkey(LinkedList(items), operation, testNumber, trueMonkey, falseMonkey)
        }

    override fun part1(): Any {
        val monkeys = getMonkeys()
        repeat(20) {
            monkeys.forEach {
                it.performTurn(monkeys) { value -> value / 3 }
            }
        }
        return monkeys.map(Monkey::inspectedItems).sortedDescending().take(2).product()
    }

    override fun part2(): Any {
        val monkeys = getMonkeys()
        val divisor = monkeys.map(Monkey::testNumber).product()
        repeat(10000) {
            monkeys.forEach {
                it.performTurn(monkeys) { value -> value % divisor }
            }
        }
        return monkeys.map(Monkey::inspectedItems).sortedDescending().take(2).product()
    }
}