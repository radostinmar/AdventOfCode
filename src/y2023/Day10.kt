package y2023

import Day
import util.Direction

object Day10 : Day() {

    fun visualize() {
        val charMap = mapOf(
            '|' to '┃',
            'J' to '┛',
            'L' to '┗',
            '7' to '┓',
            'F' to '┏',
            '-' to '━'
        )
        val loop = getLoop()
        val inside = lines.flatMapIndexed { row, line ->
            var inside = false
            line.mapIndexedNotNull { col, char ->
                val node = Node(row, col)
                val inLoop = node in loop
                val shouldChange = inLoop && node.hasTopDirection
                if (shouldChange) inside = !inside
                if (!inLoop && inside) row to col else null
            }
        }.toSet()
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                val node = Node(row, col)
                val newChar = when {
                    c == 'S' -> 'S'
                    node in loop -> charMap[c] ?: ' '
                    (row to col) in inside -> '#'
                    else -> ' '
                }
                print(newChar)
            }
            println("")
        }
    }

    private data class Node(
        val x: Int,
        val y: Int,
        val enteredFrom: Direction? = null
    ) {
        val hasTopDirection by lazy { Direction.NORTH in directions }
        private val coordinates = x to y
        private val pipe = lines[x][y]
        private val directions: List<Direction> by lazy {
            when (pipe) {
                'S' -> listOfNotNull(
                    if (Direction.SOUTH in top.directions) Direction.NORTH else null,
                    if (Direction.NORTH in bottom.directions) Direction.SOUTH else null,
                    if (Direction.WEST in right.directions) Direction.EAST else null,
                    if (Direction.EAST in left.directions) Direction.WEST else null
                )

                '7' -> listOf(Direction.WEST, Direction.SOUTH)
                'F' -> listOf(Direction.EAST, Direction.SOUTH)
                'L' -> listOf(Direction.EAST, Direction.NORTH)
                'J' -> listOf(Direction.WEST, Direction.NORTH)
                '|' -> listOf(Direction.NORTH, Direction.SOUTH)
                '-' -> listOf(Direction.WEST, Direction.EAST)
                else -> throw IllegalStateException()
            }
        }
        private val top by lazy { Node(x = x - 1, y = y, enteredFrom = Direction.SOUTH) }
        private val bottom by lazy { Node(x = x + 1, y = y, enteredFrom = Direction.NORTH) }
        private val left by lazy { Node(x = x, y = y - 1, enteredFrom = Direction.EAST) }
        private val right by lazy { Node(x = x, y = y + 1, enteredFrom = Direction.WEST) }

        fun getNext(): Node? =
            when (directions.first { it != enteredFrom }) {
                Direction.WEST -> left
                Direction.EAST -> right
                Direction.NORTH -> top
                Direction.SOUTH -> bottom
            }.let { if (it.pipe == 'S') null else it }

        override fun equals(other: Any?): Boolean =
            other is Node && this.coordinates == other.coordinates

        override fun hashCode(): Int = coordinates.hashCode()
    }

    private fun getLoop(): Set<Node> {
        val start = lines.withIndex().firstNotNullOf { row ->
            row.value.withIndex().find { col -> col.value == 'S' }
                ?.let { Node(x = row.index, y = it.index) }
        }
        val loopSequence = generateSequence(start, Node::getNext)
        return loopSequence.toSet()
    }

    override fun part1(): Number = getLoop().size / 2

    override fun part2(): Number = getLoop().let { loop ->
        lines.withIndex().sumOf { row ->
            var inside = false
            row.value.withIndex().count { col ->
                val node = Node(row.index, col.index)
                val inLoop = node in loop
                val shouldChange = inLoop && node.hasTopDirection
                if (shouldChange) inside = !inside
                !inLoop && inside
            }
        }
    }
}