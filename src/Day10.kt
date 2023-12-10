object Day10 : Day() {

    private enum class Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private data class Node(
        val x: Int,
        val y: Int,
        val enteredFrom: Direction? = null
    ) {
        val hasTopDirection by lazy { Direction.TOP in directions }
        private val coordinates = x to y
        private val pipe = lines[x][y]
        private val directions: List<Direction> by lazy {
            when (pipe) {
                'S' -> listOfNotNull(
                    if (Direction.BOTTOM in top.directions) Direction.TOP else null,
                    if (Direction.TOP in bottom.directions) Direction.BOTTOM else null,
                    if (Direction.LEFT in right.directions) Direction.RIGHT else null,
                    if (Direction.RIGHT in left.directions) Direction.LEFT else null
                )

                '7' -> listOf(Direction.LEFT, Direction.BOTTOM)
                'F' -> listOf(Direction.RIGHT, Direction.BOTTOM)
                'L' -> listOf(Direction.RIGHT, Direction.TOP)
                'J' -> listOf(Direction.LEFT, Direction.TOP)
                '|' -> listOf(Direction.TOP, Direction.BOTTOM)
                '-' -> listOf(Direction.LEFT, Direction.RIGHT)
                else -> throw IllegalStateException()
            }
        }
        private val top by lazy { Node(x = x - 1, y = y, enteredFrom = Direction.BOTTOM) }
        private val bottom by lazy { Node(x = x + 1, y = y, enteredFrom = Direction.TOP) }
        private val left by lazy { Node(x = x, y = y - 1, enteredFrom = Direction.RIGHT) }
        private val right by lazy { Node(x = x, y = y + 1, enteredFrom = Direction.LEFT) }

        fun getNext(): Node? =
            when (directions.first { it != enteredFrom }) {
                Direction.LEFT -> left
                Direction.RIGHT -> right
                Direction.TOP -> top
                Direction.BOTTOM -> bottom
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