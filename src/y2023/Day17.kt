package y2023

import Day
import util.Direction
import util.Node
import util.nodeTo
import java.util.*

object Day17 : Day(isTest = false) {

    private interface HashData {
        val node: Node
        val direction: Direction?
        val steps: Int
    }

    private data class HashDataImpl(
        override val node: Node,
        override val direction: Direction?,
        override val steps: Int
    ) : HashData

    private data class State(
        val hashData: HashDataImpl,
        val cost: Int
    ) : Comparable<State>, HashData by hashData {
        override fun compareTo(other: State): Int = this.cost.compareTo(other.cost)
    }

    private fun solve(minSteps: Int, maxSteps: Int): Int {
        val start = 0 nodeTo 0
        val end = lines.lastIndex nodeTo lines.first().lastIndex
        val queue = PriorityQueue<State>()
        val visited = hashSetOf<HashDataImpl>()
        queue.add(State(HashDataImpl(node = start, direction = null, steps = 0), cost = 0))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.hashData in visited) {
                continue
            }
            visited.add(current.hashData)
            if (current.steps >= minSteps && current.node == end) {
                return current.cost
            }

            Direction.entries.forEach { newDirection ->
                current.direction?.let { direction ->
                    if (direction.opposite == newDirection) {
                        return@forEach
                    }
                    if (current.steps < minSteps && newDirection != current.direction) {
                        return@forEach
                    }
                }

                val newSteps = if (newDirection == current.direction) current.steps + 1 else 1
                if (newSteps > maxSteps) {
                    return@forEach
                }
                val newNode = current.node + newDirection
                if (!newNode.isInBounds(lines.lastIndex, lines.first().lastIndex)) {
                    return@forEach
                }
                val newCost = current.cost + lines[newNode.x][newNode.y].digitToInt()
                queue.add(State(HashDataImpl(newNode, newDirection, newSteps), newCost))
            }
        }
        return -1
    }

    override fun part1(): Any = solve(minSteps = 1, maxSteps = 3)

    override fun part2(): Any = solve(minSteps = 4, maxSteps = 10)
}