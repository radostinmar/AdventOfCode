package util

enum class Direction(val change: Point) {
    NORTH(-1 pTo 0), EAST(0 pTo 1), SOUTH(1 pTo 0), WEST(0 pTo -1);

    val opposite: Direction by lazy {
        when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }
}