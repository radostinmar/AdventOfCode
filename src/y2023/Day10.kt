package y2023

import Day
import util.Direction
import util.Point
import util.get
import util.pTo

object Day10 : Day(year = 2023) {

    private data class PipeNode(
        val point: Point,
        val enteredFrom: Direction? = null
    ) {
        val hasTopDirection by lazy { Direction.NORTH in directions }
        private val pipe = lines[point]
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
        private val top by lazy { PipeNode(point + Direction.NORTH, enteredFrom = Direction.SOUTH) }
        private val bottom by lazy { PipeNode(point + Direction.SOUTH, enteredFrom = Direction.NORTH) }
        private val left by lazy { PipeNode(point + Direction.WEST, enteredFrom = Direction.EAST) }
        private val right by lazy { PipeNode(point + Direction.EAST, enteredFrom = Direction.WEST) }

        fun getNext(): PipeNode? =
            when (directions.first { it != enteredFrom }) {
                Direction.WEST -> left
                Direction.EAST -> right
                Direction.NORTH -> top
                Direction.SOUTH -> bottom
            }.let { if (it.pipe == 'S') null else it }

        override fun equals(other: Any?): Boolean =
            other is PipeNode && this.point == other.point

        override fun hashCode(): Int = point.hashCode()
    }

    private fun getLoop(): Set<PipeNode> {
        val start = lines.withIndex().firstNotNullOf { row ->
            row.value.withIndex().find { col -> col.value == 'S' }
                ?.let { PipeNode(row.index pTo it.index) }
        }
        val loopSequence = generateSequence(start, PipeNode::getNext)
        return loopSequence.toSet()
    }

    override fun part1(): Number = getLoop().size / 2

    override fun part2(): Number = getLoop().let { loop ->
        lines.withIndex().sumOf { row ->
            var inside = false
            row.value.withIndex().count { col ->
                val pipeNode = PipeNode(row.index pTo col.index)
                val inLoop = pipeNode in loop
                val shouldChange = inLoop && pipeNode.hasTopDirection
                if (shouldChange) inside = !inside
                !inLoop && inside
            }
        }
    }
}