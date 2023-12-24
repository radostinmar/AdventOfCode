package y2023

import Day
import util.Direction

object Day16 : Day(year = 2023) {

    private data class Beam(
        val x: Int,
        val y: Int,
        val direction: Direction
    ) {
        val left: Beam
            get() = Beam(x, y - 1, Direction.WEST)

        val right: Beam
            get() = Beam(x, y + 1, Direction.EAST)

        val top: Beam
            get() = Beam(x - 1, y, Direction.NORTH)

        val bottom: Beam
            get() = Beam(x + 1, y, Direction.SOUTH)

    }

    private fun visit(beam: Beam, tested: MutableSet<Beam>) {
        if (beam.x !in lines.indices || beam.y !in lines.first().indices) {
            return
        }
        if (beam in tested) {
            return
        }
        tested.add(beam)
        when (lines[beam.x][beam.y]) {
            '.' -> when (beam.direction) {
                Direction.WEST -> visit(beam.left, tested)
                Direction.EAST -> visit(beam.right, tested)
                Direction.NORTH -> visit(beam.top, tested)
                Direction.SOUTH -> visit(beam.bottom, tested)
            }

            '|' -> when (beam.direction) {
                Direction.WEST -> {
                    visit(beam.top, tested)
                    visit(beam.bottom, tested)
                }

                Direction.EAST -> {
                    visit(beam.top, tested)
                    visit(beam.bottom, tested)
                }

                Direction.NORTH -> visit(beam.top, tested)
                Direction.SOUTH -> visit(beam.bottom, tested)
            }

            '-' -> when (beam.direction) {
                Direction.WEST -> visit(beam.left, tested)
                Direction.EAST -> visit(beam.right, tested)
                Direction.NORTH -> {
                    visit(beam.left, tested)
                    visit(beam.right, tested)
                }

                Direction.SOUTH -> {
                    visit(beam.left, tested)
                    visit(beam.right, tested)
                }
            }

            '/' -> when (beam.direction) {
                Direction.WEST -> visit(beam.bottom, tested)
                Direction.EAST -> visit(beam.top, tested)
                Direction.NORTH -> visit(beam.right, tested)
                Direction.SOUTH -> visit(beam.left, tested)
            }

            '\\' -> when (beam.direction) {
                Direction.WEST -> visit(beam.top, tested)
                Direction.EAST -> visit(beam.bottom, tested)
                Direction.NORTH -> visit(beam.left, tested)
                Direction.SOUTH -> visit(beam.right, tested)
            }
        }
    }

    override fun part1(): Any {
        val tested = mutableSetOf<Beam>()
        visit(Beam(0, 0, Direction.EAST), tested)
        return tested.map { it.x to it.y }.toSet().size
    }

    override fun part2(): Any {
        val startingTop = lines.first().indices.map {
            Beam(0, it, Direction.SOUTH)
        }
        val startingBottom = lines.first().indices.map {
            Beam(lines.lastIndex, it, Direction.NORTH)
        }
        val startingLeft = lines.indices.map {
            Beam(it, 0, Direction.EAST)
        }
        val startingRight = lines.indices.map {
            Beam(it, lines.first().lastIndex, Direction.WEST)
        }
        return (startingLeft + startingRight + startingTop + startingBottom).maxOf {
            val tested = mutableSetOf<Beam>()
            visit(it, tested)
            tested.map { beam -> beam.x to beam.y }.toSet().size
        }
    }
}