package y2023

import Day
import java.math.BigDecimal

object Day24 : Day(isTest = false) {

    private data class PointBigDecimal(val x: BigDecimal, val y: BigDecimal) {
        operator fun plus(other: PointBigDecimal): PointBigDecimal = PointBigDecimal(this.x + other.x, this.y + other.y)
    }

    private data class Point3DBigDecimal(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
        operator fun plus(other: Point3DBigDecimal): Point3DBigDecimal =
            Point3DBigDecimal(this.x + other.x, this.y + other.y, this.z + other.z)

        fun to2D(): PointBigDecimal = PointBigDecimal(x, y)
    }

    private fun findIntersection(
        a1: PointBigDecimal,
        a2: PointBigDecimal,
        a3: PointBigDecimal,
        a4: PointBigDecimal
    ): PointBigDecimal? {
        val xDiff = PointBigDecimal(a1.x - a2.x, a3.x - a4.x)
        val yDiff = PointBigDecimal(a1.y - a2.y, a3.y - a4.y)

        fun det(a: PointBigDecimal, b: PointBigDecimal) =
            a.x * b.y - a.y * b.x

        val div = det(xDiff, yDiff)
        if (div.compareTo(BigDecimal.ZERO) == 0) {
            return null
        }

        val d = PointBigDecimal(det(a1, a2), det(a3, a4))
        val x = det(d, xDiff) / div
        val y = det(d, yDiff) / div
        return PointBigDecimal(x, y)
    }

    private data class HailStone(
        val position: Point3DBigDecimal,
        val velocity: Point3DBigDecimal
    )

    override fun part1(): Any {
        val intersectionRange = if (isTest) {
            BigDecimal(7)..BigDecimal(27)
        } else {
            BigDecimal(200000000000000)..BigDecimal(400000000000000)
        }
        val hailStones = lines.map { line ->
            val values = line.split("\\s*([@,])\\s*".toRegex()).map { it.toBigDecimal() }
            HailStone(
                Point3DBigDecimal(values[0], values[1], values[2]),
                Point3DBigDecimal(values[3], values[4], values[5])
            )
        }
        return hailStones.withIndex().sumOf { (index, hailStoneA) ->
            hailStones.subList(index + 1, hailStones.size).count { hailStoneB ->
                val secondPositionA = hailStoneA.position + hailStoneA.velocity
                val secondPositionB = hailStoneB.position + hailStoneB.velocity
                findIntersection(
                    hailStoneA.position.to2D(),
                    secondPositionA.to2D(),
                    hailStoneB.position.to2D(),
                    secondPositionB.to2D(),
                )?.let {
                    (it.x - hailStoneA.position.x).compareTo(BigDecimal.ZERO) == hailStoneA.velocity.x.compareTo(BigDecimal.ZERO)
                            && (it.x - hailStoneB.position.x).compareTo(BigDecimal.ZERO) == hailStoneB.velocity.x.compareTo(BigDecimal.ZERO)
                            && it.x in intersectionRange
                            && it.y in intersectionRange
                } ?: false
            }
        }
    }

    override fun part2(): Any =
        "Solved using SageMath. Generate 9 equations from the first 3 lines. Check day24part2.sage. Paste contents into https://sagecell.sagemath.org/"
}
