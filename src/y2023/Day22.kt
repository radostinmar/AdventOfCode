package y2023

import Day
import util.Point3D
import util.toInts

object Day22 : Day(isTest = false) {

    private fun getInitialBricks(): List<Brick> =
        lines.map { line ->
            val (start, end) = line.split('~').map {
                val (x, y, z) = it.split(',').toInts()
                Point3D(x, y, z)
            }
            Brick(start, end)
        }.sortedBy { it.start.z }

    private fun disintegrate(bricks: List<Brick>, disintegrated: List<Brick>, below: Map<Brick, List<Brick>>): Int {
        val allDisintegrated = disintegrated + bricks
        val toDisintegrate =
            below.filter { entry ->
                entry.key !in allDisintegrated
                        && entry.value.isNotEmpty()
                        && entry.value.all { it in allDisintegrated }
            }.map { it.key }
        if (toDisintegrate.isEmpty()) return 0
        return toDisintegrate.size + disintegrate(toDisintegrate, allDisintegrated, below)
    }

    data class Brick(val start: Point3D, val end: Point3D)

    private fun Brick.overlaps(other: Brick): Boolean =
        (other.start.x <= this.end.x && this.start.x <= other.end.x) &&
                (other.start.y <= this.end.y && this.start.y <= other.end.y)

    private fun Brick.fall(placed: List<Brick>): Pair<Brick, List<Brick>> {
        var lowestZ = this.start.z
        while (true) {
            if (lowestZ == 1) {
                val diff = this.start.z - lowestZ
                return this.copy(
                    start = start.copy(z = this.start.z - diff),
                    end = end.copy(z = this.end.z - diff)
                ) to emptyList()
            }
            val supportedBy = placed.filter { it.end.z == lowestZ - 1 && this.overlaps(it) }
            if (supportedBy.isNotEmpty()) {
                val diff = this.start.z - lowestZ
                return this.copy(
                    start = start.copy(z = this.start.z - diff),
                    end = end.copy(z = this.end.z - diff)
                ) to supportedBy
            } else {
                lowestZ--
            }
        }
    }

    override fun part1(): Any {
        val bricks = getInitialBricks()
        val supports = mutableMapOf<Brick, MutableList<Brick>>()
        val placed = mutableListOf<Brick>()
        bricks.forEach { brick ->
            brick.fall(placed).also {
                placed.add(it.first)
                supports[it.first] = mutableListOf()
                if (it.second.size == 1) {
                    supports.getValue(it.second.first()).add(it.first)
                }
            }
        }
        return supports.count { it.value.isEmpty() }
    }

    override fun part2(): Any {
        val bricks = getInitialBricks()
        val placed = mutableListOf<Brick>()
        val below = bricks.associate { brick ->
            brick.fall(placed).also {
                placed.add(it.first)
            }
        }
        return placed.withIndex().sumOf {
            disintegrate(listOf(it.value), emptyList(), below)
        }
    }
}
