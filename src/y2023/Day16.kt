package y2023

import Day

object Day16 : Day(isTest = false) {

    enum class Direction {
        LEFT, RIGHT, UP, DOWN
    }

    private data class Beam(
        val x: Int,
        val y: Int,
        val direction: Direction
    ) {
        val left: Beam
            get() = Beam(x, y - 1, Direction.LEFT)

        val right: Beam
            get() = Beam(x, y + 1, Direction.RIGHT)

        val top: Beam
            get() = Beam(x - 1, y, Direction.UP)

        val bottom: Beam
            get() = Beam(x + 1, y, Direction.DOWN)

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
                Direction.LEFT -> visit(beam.left, tested)
                Direction.RIGHT -> visit(beam.right, tested)
                Direction.UP -> visit(beam.top, tested)
                Direction.DOWN -> visit(beam.bottom, tested)
            }

            '|' -> when (beam.direction) {
                Direction.LEFT -> {
                    visit(beam.top, tested)
                    visit(beam.bottom, tested)
                }

                Direction.RIGHT -> {
                    visit(beam.top, tested)
                    visit(beam.bottom, tested)
                }

                Direction.UP -> visit(beam.top, tested)
                Direction.DOWN -> visit(beam.bottom, tested)
            }

            '-' -> when (beam.direction) {
                Direction.LEFT -> visit(beam.left, tested)
                Direction.RIGHT -> visit(beam.right, tested)
                Direction.UP -> {
                    visit(beam.left, tested)
                    visit(beam.right, tested)
                }

                Direction.DOWN -> {
                    visit(beam.left, tested)
                    visit(beam.right, tested)
                }
            }

            '/' -> when (beam.direction) {
                Direction.LEFT -> visit(beam.bottom, tested)
                Direction.RIGHT -> visit(beam.top, tested)
                Direction.UP -> visit(beam.right, tested)
                Direction.DOWN -> visit(beam.left, tested)
            }

            '\\' -> when (beam.direction) {
                Direction.LEFT -> visit(beam.top, tested)
                Direction.RIGHT -> visit(beam.bottom, tested)
                Direction.UP -> visit(beam.left, tested)
                Direction.DOWN -> visit(beam.right, tested)
            }
        }
    }

    override fun part1(): Any {
        val tested = mutableSetOf<Beam>()
        visit(Beam(0, 0, Direction.RIGHT), tested)
        return tested.map { it.x to it.y }.toSet().size
    }

    override fun part2(): Any {
        val startingTop = lines.first().indices.map {
            Beam(0, it, Direction.DOWN)
        }
        val startingBottom = lines.first().indices.map {
            Beam(lines.lastIndex, it, Direction.UP)
        }
        val startingLeft = lines.indices.map {
            Beam(it, 0, Direction.RIGHT)
        }
        val startingRight = lines.indices.map {
            Beam(it, lines.first().lastIndex, Direction.LEFT)
        }
        return (startingLeft + startingRight + startingTop + startingBottom).maxOf {
            val tested = mutableSetOf<Beam>()
            visit(it, tested)
            tested.map { beam -> beam.x to beam.y }.toSet().size
        }
    }
}