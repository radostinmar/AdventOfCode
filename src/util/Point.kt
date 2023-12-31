package util

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point = Point(this.x + other.x, this.y + other.y)

    operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)

    operator fun plus(direction: Direction): Point = this + direction.change

    operator fun times(times: Int): Point = Point(this.x * times, this.y * times)

    fun isInBounds(xLimit: Int, yLimit: Int): Boolean =
        x in 0..xLimit && y in 0..yLimit
}

operator fun List<String>.get(point: Point): Char = this[point.x][point.y]

operator fun <T> List<List<T>>.get(point: Point): T = this[point.x][point.y]

infix fun Int.pTo(that: Int): Point = Point(this, that)