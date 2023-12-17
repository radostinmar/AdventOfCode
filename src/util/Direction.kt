package util

enum class Direction(val change: Node) {
    NORTH(-1 nodeTo 0), EAST(0 nodeTo 1), SOUTH(1 nodeTo 0), WEST(0 nodeTo -1)
}