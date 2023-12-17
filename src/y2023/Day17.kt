package y2023

import Day
import util.Direction
import util.Node
import util.nodeTo
import java.util.*

object Day17 : Day() {

    private data class State(val node: Node, val direction: Direction, val steps: Int)

    private data class StateWithCost(val state: State, val cost: Int) : Comparable<StateWithCost> {
        override fun compareTo(other: StateWithCost): Int = this.cost.compareTo(other.cost)
    }

    private fun solve(minSteps: Int, maxSteps: Int): Int {
        val start = 0 nodeTo 0
        val end = lines.lastIndex nodeTo lines.first().lastIndex
        val queue = PriorityQueue<StateWithCost>()
        val visited = hashSetOf<State>()
        queue.add(StateWithCost(State(start, Direction.EAST, 1), 0))
        queue.add(StateWithCost(State(start, Direction.SOUTH, 1), 0))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.state in visited) {
                continue
            }
            visited.add(current.state)
            val newNode = current.state.node + current.state.direction.change
            if (!newNode.isInBounds(lines.lastIndex, lines.first().lastIndex)) {
                continue
            }
            val newCost = current.cost + lines[newNode.x][newNode.y].digitToInt()
            if (current.state.steps >= minSteps && newNode == end) {
                return newCost
            }

            Direction.entries.forEach { newDirection ->
                if (current.state.direction.change + newDirection.change == 0 nodeTo 0) {
                    return@forEach
                }
                if (current.state.steps < minSteps && newDirection != current.state.direction) {
                    return@forEach
                }
                val newSteps = if (newDirection == current.state.direction) current.state.steps + 1 else 1
                if (newSteps > maxSteps) {
                    return@forEach
                }
                queue.add(StateWithCost(State(newNode, newDirection, newSteps), newCost))
            }
        }
        return -1
    }

    override fun part1(): Any =
        solve(minSteps = 1, maxSteps = 3)

    override fun part2(): Any =
        solve(minSteps = 4, maxSteps = 10)
}